(ns pyroclast-clojure.v1.client
  (:require [kvlt.core :as http]
            [clojure.set :as cset]
            [promesa.core :as p]
            #?(:cljs [goog.string :as gstring])
            #?(:cljs [goog.string.format])))
#?(:cljs
   (defn format
         "Formats a string using goog.string.format.
          e.g: (format \"Cost: %.2f\" 10.0234)"
         [fmt & args]
         (apply gstring/format fmt args)))

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
   (p/chain (http/request!
             {:url (topic-produce-url config)
              :method :post
              :type :json
              :headers {"Authorization" write-key}
              :form-params event
              :as :auto})
            (fn [{:keys [status]}]
              (if (= status 201) true false)))))

(def conf {:pyroclast.topic/id "topic-530ed060-fc0a-45a8-adca-06ce6b4683d0"
           :pyroclast.archiver/bucket "gvickers-archiver-us-west-2"
           :pyroclast.topic/read-key "4585b016-967b-4a0e-822d-6a10a0c4112a"
           :pyroclast.topic/write-key "5719d08f-a22d-4cae-83aa-2a47a8242dba"
           :pyroclast.api/region "us-west-2"})

;;@(topic-send-event! conf {:value {:foo "bar"}})

(defn topic-send-events!
  "Send a batch of events to a Pyroclast topic.
  Event must be of the form [{:value ...} {:value ...}].
  Returns a dereffable deferred."
  ([{:keys [pyroclast.topic/write-key pyroclast.topic/id] :as config} events]
   (assert (every? #(contains? % :value) events) "All events require a value key")
   (p/chain (http/request!
             {:url (topic-bulk-produce-url config)
              :method :post
              :type :json
              :headers {"Authorization" write-key}
              :form-params events
              :as :auto})
            (fn [{:keys [status]}]
              (= 201 status)))))

(def subscribe-remap {:group-id :pyroclast.consumer/group-id
                      :consumer-instance-id :pyroclast.consumer-instance/id})

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
   @(p/chain (http/request!
              {:url (topic-subscribe-url config consumer-group-name)
               :method :post
               :type :json
               :headers {"Authorization" read-key}
               :body  {"auto.offset.reset" auto-offset-reset
                       "partitions" partitions
                       "bucket" bucket}
               :as :auto})
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
    (p/chain  (http/request!
               {:url (topic-poll-url config group-id consumer-instance-id)
                :method :post
                :type :json
                :headers {"Authorization" read-key}
                :as :auto})
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
    (p/chain (http/request!
              {:url (topic-information-url config group-id consumer-instance-id)
               :method :get
               :type :json
               :headers {"Authorization" read-key}
               :as :json})
             (fn [{:keys [status body]}]
               (when (= 200 status)
                 (println body)
                 (into {}
                       (map (fn [[k v]] ;; clj-http auto converts keys to kw's, hack around that for now.
                              [(name k) (into {}
                                              (map (fn [[k v]]
                                                     [(name k) v]))
                                              v)]))
                       (:positions body)))))))

(taoensso.timbre/set-level! :info)
(defn topic-consumer-commit-offsets
  "Commit a consumer group instance's current offset. Ensures that after this operation
  succeeds, new subscriptions will not see records before the current offset."
  [{:keys [pyroclast.topic/read-key pyroclast.topic/id
           pyroclast.consumer/group-id] :as config}]
  (let [consumer-instance-id (:pyroclast.consumer-instance/id config)]
    @(p/chain (http/request!
               {:url (topic-commit-url config group-id consumer-instance-id)
                :method :post
                :type :json
                :headers {"Authorization" read-key}
                :as :auto})
              (fn [{:keys [status]}]
                (= status 200)))))

(defn- topic-consumer-seek-simple
  [{:keys [pyroclast.topic/read-key pyroclast.topic/id
           pyroclast.consumer/group-id] :as config}
   direction]
  (assert (#{"beginning" "end"} direction) "seek direction must be one of :beginning or :end")
  (let [consumer-instance-id (:pyroclast.consumer-instance/id config)]
    @(p/chain (http/request!
                {:url (topic-seek-url config group-id consumer-instance-id direction)
                 :method :post
                 :type :json
                 :headers {"Content-type" "application/json"
                           "Authorization" read-key}
                 :as :text})
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
    @(p/chain (http/request!
                {:url (topic-seek-url config group-id consumer-instance-id)
                 :async? true
                 :method :post
                 :type :json
                 :headers {"Authorization" read-key}
                 :form-params partition-directives
                 :as :auto})
              (fn [{:keys [status]}]
                (= status 200)))))

(defn ^:export hello_world []
  #?(:cljs (js/console.log "Hello world!")))