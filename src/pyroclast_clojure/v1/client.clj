(ns pyroclast-clojure.v1.client
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.set :as cset]
            [promesa.core :as p]))

(def default-region "us-west-2")

(defn- base-url [{:keys [pyroclast.api/region pyroclast.api/endpoint] :or {region default-region} :as config}]
  (if endpoint
    endpoint
    (str (format "https://api.%s.pyroclast.io" region))))

(defn topic-produce-url [{:keys [pyroclast.topic/id] :as config}]
  (format "%s/v1/topics/%s/produce" (base-url config) id))

(defn topic-bulk-produce-url [{:keys [pyroclast.topic/id] :as config}]
  (format "%s/v1/topics/%s/bulk-produce" (base-url config) id))

(defn topic-subscribe-url [{:keys [pyroclast.topic/id] :as config} consumer-group-name]
  (format "%s/v1/topics/%s/consumers/%s/subscribe" (base-url config) id consumer-group-name))

(defn topic-poll-url [{:keys [pyroclast.topic/id] :as config} consumer-group-name consumer-instance-id]
  (format "%s/v1/topics/%s/consumers/%s/instances/%s/poll" (base-url config)
          id consumer-group-name consumer-instance-id))

(defn topic-information-url [{:keys [pyroclast.topic/id] :as config} consumer-group-name consumer-instance-id]
  (format "%s/v1/topics/%s/consumers/%s/instances/%s" (base-url config)
          id consumer-group-name consumer-instance-id))

(defn topic-commit-url [{:keys [pyroclast.topic/id] :as config} consumer-group-name consumer-instance-id]
  (format "%s/v1/topics/%s/consumers/%s/instances/%s/commit" (base-url config)
          id consumer-group-name consumer-instance-id))

(defn topic-seek-url
  ([{:keys [pyroclast.topic/id] :as config} consumer-group-name consumer-instance-id]
   (topic-seek-url config consumer-group-name consumer-instance-id nil))
  ([{:keys [pyroclast.topic/id] :as config} consumer-group-name consumer-instance-id direction]
   (if direction
     (format "%s/v1/topics/%s/consumers/%s/instances/%s/seek/%s" (base-url config)
             id consumer-group-name consumer-instance-id direction)
     (format "%s/v1/topics/%s/consumers/%s/instances/%s/seek" (base-url config)
             id consumer-group-name consumer-instance-id))))

(defn deployment-aggregates-url
  [{:keys [pyroclast.deployment/id] :as config}]
  (format "%s/v1/deployments/%s/aggregates" (base-url config) id))

(defn deployment-aggregate-url
  [{:keys [pyroclast.deployment/id] :as config} aggregate-name]
  (format "%s/v1/deployments/%s/aggregates/%s" (base-url config) id aggregate-name))

(defn topic-send-event!
  "Send a single event to a Pyroclast topic.
  Event must be of the form {:value ...}.
  Returns a dereffable deferred."
  ([{:keys [pyroclast.topic/write-key pyroclast.topic/id] :as config} event]
   (assert (contains? event :value) "Event requires value key")
   (p/chain (p/promise
             (fn [resolve reject]
               (http/post (topic-produce-url config)
                          {:async? true
                           :accept :json
                           :headers {"Content-type" "application/json"
                                     "Authorization" write-key}
                           :body (json/generate-string event)}
                          resolve
                          reject)))
            (fn [{:keys [status]}]
              (= 201 status)))))

(defn topic-send-events!
  "Send a batch of events to a Pyroclast topic.
  Event must be of the form [{:value ...} {:value ...}].
  Returns a dereffable deferred."
  ([{:keys [pyroclast.topic/write-key pyroclast.topic/id] :as config} events]
   (assert (every? #(contains? % :value) events) "All events require a value key")
   (p/chain (p/promise
             (fn [resolve reject]
               (http/post (topic-bulk-produce-url config)
                          {:async? true
                           :accept :json
                           :headers {"Content-type" "application/json"
                                     "Authorization" write-key}
                           :body (json/generate-string events)}
                          resolve
                          reject)))
            (fn [{:keys [status]}]
              (= 201 status)))))



(def subscribe-remap {:group-id :pyroclast.consumer/group-id
                      :consumer-instance-id :pyroclast.consumer-instance/id})
(defn consumer-instance-map [config body]
  (let [resp (json/parse-string body true)
        remapped (cset/rename-keys resp subscribe-remap)]
    (merge remapped config)))

(defn topic-subscribe
  "Creates a new consumer instance, returns a consumer instance map able to
  manipulate your Pyroclast consumer.

  Options:
  :auto-offset-reset - One of `:earliest` or `:latest`. Defaults to :earliest
  :partitions - A list of partitions to consumer from. `:all` will consume from all partitions. Defaults to :all

  Returns a topic instance map."
  ([config consumer-group-name] (topic-subscribe config consumer-group-name {}))
  ([{:keys [pyroclast.topic/read-key pyroclast.topic/id pyroclast.archiver/bucket] :as config}
    consumer-group-name
    {:keys [auto-offset-reset partitions]
     :or {auto-offset-reset "earliest"
          partitions "all"}}]
   (when (not (re-matches #"[a-zA-Z0-9-_]+" consumer-group-name))
     (throw (ex-info "Consumer group name must be a non-empty string of alphanumeric characters." {})))
   (assert (#{"earliest" "latest"} auto-offset-reset) ":offset options must be one of \"earliest\" or \"latest\"")
   @(p/chain (p/promise (fn [resolve reject]
                          (http/post (topic-subscribe-url config consumer-group-name)
                                     {:async? true
                                      :accept :json
                                      :headers {"Content-type" "application/json"
                                                "Authorization" read-key}
                                      :body (json/generate-string {"auto.offset.reset" auto-offset-reset
                                                                   "partitions" partitions
                                                                   "bucket" bucket})
                                      :as :auto}
                                     resolve
                                     reject)))
             (fn [{:keys [status body]}]
               (when (= 201 status)
                 (merge (cset/rename-keys body subscribe-remap)
                        config))))))

(defn topic-consumer-poll!
  "Takes a Pyroclast API config and a consumer instance map as returned by
  `topic-subscribe`. Polls topic for records. Returns a dereffable
  deferred with zero or more records."
  [{:keys [pyroclast.topic/read-key pyroclast.topic/id
           pyroclast.consumer/group-id] :as config}]
  (let [consumer-instance-id (:pyroclast.consumer-instance/id config)]
    (assert group-id "Must supply a consumer instance map with a :group-id")
    (assert consumer-instance-id "Must supply a consumer instance map with a :consumer-instance-id")
    (p/chain (p/promise (fn [resolve reject]
                          (http/post (topic-poll-url config group-id consumer-instance-id)
                                     {:async? true
                                      :accept :json
                                      :throw-exceptions false
                                      :headers {"Content-type" "application/json"
                                                "Authorization" read-key}
                                      :as :auto}
                                     resolve
                                     reject)))
             (fn [{:keys [status body]}]
               (when (= 200 status)
                 body)))))

(defn topic-consumer-information
  "Takes a Pyroclast API consumer instance map as returned by
  `topic-subscribe`. Returns information about this consumer instance.
  Returns an IDeref."
  [{:keys [pyroclast.topic/read-key pyroclast.topic/id
           pyroclast.consumer/group-id] :as config}]
  (let [consumer-instance-id (:pyroclast.consumer-instance/id config)]
    (assert group-id "Must supply a consumer instance map with a :group-id")
    (assert consumer-instance-id "Must supply a consumer instance map with a :consumer-instance-id")
    (p/chain (p/promise (fn [resolve reject]
                          (http/get (topic-information-url config group-id consumer-instance-id)
                                    {:async? true
                                     :accept :json
                                     :throw-exceptions false
                                     :headers {"Content-type" "application/json"
                                               "Authorization" read-key}
                                     :as :auto}
                                    resolve
                                    reject)))
             (fn [{:keys [status body]}]
               (when (= 200 status)
                 (into {}
                       (map (fn [[k v]] ;; clj-http auto converts keys to kw's, hack around that for now.
                              [(name k) (into {}
                                              (map (fn [[k v]]
                                                     [(name k) v]))
                                              v)]))
                       (:positions body)))))))

(defn topic-consumer-commit-offsets
  "Commit a consumer group instance's current offset. Ensures that after this operation
  succeeds, new subscriptions will not see records before the current offset."
  [{:keys [pyroclast.topic/read-key pyroclast.topic/id
           pyroclast.consumer/group-id] :as config}]
  (let [consumer-instance-id (:pyroclast.consumer-instance/id config)]
    @(p/chain (p/promise (fn [resolve reject]
                           (http/post (topic-commit-url config group-id consumer-instance-id)
                                      {:async? true
                                       :accept :json
                                       :headers {"Content-type" "application/json"
                                                 "Authorization" read-key}
                                       :as :auto}
                                      resolve
                                      reject)))
              (fn [{:keys [status]}]
                (= status 200)))))

(defn- topic-consumer-seek-simple
  [{:keys [pyroclast.topic/read-key pyroclast.topic/id
           pyroclast.consumer/group-id] :as config}
   direction]
  (assert (#{"beginning" "end"} direction) "seek direction must be one of :beginning or :end")
  (let [consumer-instance-id (:pyroclast.consumer-instance/id config)]
    @(p/chain (p/promise
               (fn [resolve reject]
                 (http/post (topic-seek-url config group-id consumer-instance-id direction)
                            {:async? true
                             :accept :json
                             :headers {"Content-type" "application/json"
                                       "Authorization" read-key}
                             :as :text}
                            resolve
                            reject)))
              (fn [{:keys [status]}]
                (= status 200)))))

(defn topic-consumer-seek-beginning
  "Seek a consumer across all of it's assigned partitions to the last lowest
  uncommited offset."
  [consumer-instance-map]
  (topic-consumer-seek-simple consumer-instance-map "beginning"))



(defn topic-consumer-seek-end
  "Seek a consumer across all of it's assigned partitions to the last highest
  uncommited offset."
  [consumer-instance-map]
  (topic-consumer-seek-simple consumer-instance-map "end"))

(defn topic-consumer-seek
  "Seek by offset or timestamp through a topic.
  Takes a Pyroclast API config map, a consumer instance map and a
  vector of map partition directives for seeking the consumer instance on your topic.

  Each map entry in the vector specifies a partition and a seek strategy.
  `:partition` - A string denoting the partition.
  `:offset`    - An ordinal offset representing a specific uncommited position.
  `:timestamp` - A temporal offset representing the earliest uncommited position.

  Examples:
  `{:partition \"1\" :offset 12}` - seek to a specific offset.
  `{:partition \"0\" :timestamp \"1508293577\"}` - seek to a timestamp."
  [{:keys [pyroclast.topic/read-key pyroclast.topic/id
           pyroclast.consumer/group-id] :as config}
   partition-directives]
  (let [consumer-instance-id (:pyroclast.consumer-instance/id config)]
    (assert (every? :partition partition-directives) "All partition directives must contain a :partition key.")
    (assert (every? (fn [m] (or (:offset m) (:timestamp m))) partition-directives) "All partition directives must contain either a :offset or a :timestamp key")
    @(p/promise (fn [resolve reject]
                  (http/post (topic-seek-url config group-id consumer-instance-id)
                             {:async? true
                              :accept :json
                              :headers {"Content-type" "application/json"
                                        "Authorization" read-key}

                              :body (json/generate-string partition-directives)}
                             resolve
                             reject)))))
