(ns pyroclast-clojure.v1.client
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [manifold.deferred :as md]))

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

(defn- validate-event! [event]
  (when-not (contains? event :value)
    (throw (ex-info "Event requires :value key" {:event event}))))

(defn- common-response [deferred {:keys [status body] :as response}]
  (cond
    (= status 400) (md/error! deferred (ex-info "Request was malformed." {:response response}))
    (= status 401) (md/error! deferred (ex-info "API key unauthorized to perform this action." {}))
    (= status 500) (md/error! deferred (ex-info (str "Server error: " body) {}))
    :else (md/error! deferred (ex-info "Unknown problem. Open an issue on this repository if you're seeing this status." {}))))

(defn topic-send-event!
  "Send a single event to a Pyroclast topic.
  Event must be of the form {:value ...}.
  Returns a dereffable deferred."
  ([{:keys [pyroclast.topic/write-key pyroclast.topic/id] :as config} event]
   (let [promise (md/deferred)]
     (validate-event! event)
     (http/post (topic-produce-url config)
                {:async? true
                 :throw-exceptions false
                 :headers {"Content-type" "application/json"
                           "Authorization" write-key}
                 :body (json/generate-string event)}
                (fn [{:keys [status] :as resp}]
                  (if (= 200 status)
                    (md/success! promise true)
                    (common-response promise resp)))
                (partial md/error! promise))
     promise)))

(defn topic-send-events!
  "Send a batch of events to a Pyroclast topic.
  Event must be of the form [{:value ...} {:value ...}].
  Returns a dereffable deferred."
  ([{:keys [pyroclast.topic/write-key pyroclast.topic/id] :as config} events]
   (let [promise (md/deferred)]
     (run! validate-event! events)
     (http/post (topic-bulk-produce-url config)
                {:async? true
                 :throw-exceptions true
                 :headers {"Content-type" "application/json"
                           "Authorization" write-key}
                 :body (json/generate-string events)}
                (fn [{:keys [status] :as resp}]
                  (if (= 200 status)
                    (md/success! promise true)
                    (common-response promise resp)))
                (partial md/error! promise))
     promise)))

(defn topic-subscribe
  "Creates a new consumer instance, returns a consumer instance map able to
  manipulate your Pyroclast consumer.

  Options:
  :auto-offset-reset - One of `:earliest` or `:latest`. Defaults to :earliest
  :partitions - A list of partitions to consumer from. `:all` will consume from all partitions. Defaults to :all

  Returns a dereffable deferred."
  ([config consumer-group-name] (topic-subscribe config consumer-group-name {}))
  ([{:keys [pyroclast.topic/read-key pyroclast.topic/id] :as config} consumer-group-name {:keys [auto-offset-reset partitions]
                                                                                          :or {auto-offset-reset :earliest partitions :all}}]
   (when (not (re-matches #"[a-zA-Z0-9-_]+" consumer-group-name))
     (throw (ex-info "Consumer group name must be a non-empty string of alphanumeric characters." {})))
   (assert (#{:earliest :latest} auto-offset-reset) ":offset options must be one of :earliest or :latest")
   (let [promise (md/deferred)]
     (http/post (topic-subscribe-url config consumer-group-name)
                {:async? true
                 :throw-exceptions true
                 :headers {"Content-type" "application/json"
                           "Authorization" read-key}
                 :body (json/generate-string {"auto.offset.reset" auto-offset-reset "partitions" partitions})}
                (fn [{:keys [status body] :as resp}]
                  (if (= 201 status)
                    (md/success! promise (json/parse-string body true))
                    (common-response promise resp)))
                (partial md/error! promise))
     promise)))

(defn topic-consumer-poll!
  "Takes a Pyroclast API config and a consumer instance map as returned by
  `topic-subscribe`. Polls topic for records. Returns a dereffable
  deferred with zero or more records."
  [{:keys [pyroclast.topic/read-key pyroclast.topic/id] :as config}
   {:keys [group-id consumer-instance-id] :as consumer-instance-map}]
  (assert group-id "Must supply a consumer instance map with a :group-id")
  (assert consumer-instance-id "Must supply a consumer instance map with a :consumer-instance-id")
  (let [promise (md/deferred)]
    (http/post (topic-poll-url config group-id consumer-instance-id)
               {:async? true
                :throw-exceptions true
                :headers {"Content-type" "application/json"
                          "Authorization" read-key}}
               (fn [{:keys [status body] :as resp}]
                 (if (= 200 status)
                   (md/success! promise (vec (json/parse-string body)))
                   (common-response promise resp)))
               (partial md/error! promise))
    promise))

(defn topic-consumer-commit-offsets
  "Commit a consumer group instance's current offset. Ensures that after this operation
  succeeds, new subscriptions will not see records before the current offset."
  [{:keys [pyroclast.topic/read-key pyroclast.topic/id] :as config}
   {:keys [group-id consumer-instance-id] :as consumer-instance-map}]
  (let [promise (md/deferred)]
    (http/post (topic-commit-url config group-id consumer-instance-id)
               {:async? true
                :headers {"Content-type" "application/json"
                          "Authorization" read-key}
                :throw-exceptions true
                :as :text}
               (fn [{:keys [status] :as resp}]
                 (if (= 200 status)
                   (md/success! promise true)
                   (common-response promise resp)))
               (partial md/error! promise))
    ;; Probably want this to be synchronous to avoid late commits.
    @promise))

(defn- topic-consumer-seek-simple
  [{:keys [pyroclast.topic/read-key pyroclast.topic/id] :as config}
   {:keys [group-id consumer-instance-id] :as consumer-instance-map}
   direction]
  (assert (#{"beginning" "end"} direction) "seek direction must be one of :beginning or :end")
  (let [promise (md/deferred)]
    (http/post (topic-seek-url config group-id consumer-instance-id direction)
               {:async? true
                :headers {"Content-type" "application/json"
                          "Authorization" read-key}
                :throw-exceptions true
                :as :text}
               (fn [{:keys [status] :as resp}]
                 (if (= 200 status)
                   (md/success! promise true)
                   (common-response promise resp)))
               (partial md/error! promise))
    @promise))

(defn topic-consumer-seek-beginning
  "Seek a consumer across all of it's assigned partitions to the last lowest
  uncommited offset."
  [{:keys [pyroclast.topic/read-key pyroclast.topic/id] :as config}
   {:keys [group-id consumer-instance-id] :as consumer-instance-map}]
  (topic-consumer-seek-simple config consumer-instance-map "beginning"))

(defn topic-consumer-seek-end
  "Seek a consumer across all of it's assigned partitions to the last highest
  uncommited offset."
  [{:keys [pyroclast.topic/read-key pyroclast.topic/id] :as config}
   {:keys [group-id consumer-instance-id] :as consumer-instance-map}]
  (topic-consumer-seek-simple config consumer-instance-map "end"))

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
  [{:keys [pyroclast.topic/read-key pyroclast.topic/id] :as config}
   {:keys [group-id consumer-instance-id] :as consumer-instance-map}
   partition-directives]
  (assert (every? :partition partition-directives) "All partition directives must contain a :partition key.")
  (assert (every? (fn [m] (or (:offset m) (:timestamp m))) partition-directives) "All partition directives must contain either a :offset or a :timestamp key")
  (let [promise (md/deferred)]
    (http/post (topic-seek-url config group-id consumer-instance-id)
               {:async? true
                :throw-exceptions true
                :headers {"Content-type" "application/json"
                          "Authorization" read-key}

                :body (json/generate-string partition-directives)}
               (fn [{:keys [status] :as resp}]
                 (if (= 200 status)
                   (md/success! promise true)
                   (common-response promise resp)))
               (partial md/error! promise))
    @promise))

(defn deployment-fetch-aggregate
  "Return a subset of data from a specific deployment aggregate. Subset is specified
  by a `query` map.

  Query map can take the following keys:

  `:start` - a positive integer representing the lower bound timestamp on aggregate data to return.
  `:end` - a positive integer representing the upper bound timestamp on aggregate data to return.
  `:groups` - a subset of groups to return if using a session window.
  `:sort` - a boolean indicating if the aggregate should be sorted by timestamp."
  [{:keys [pyroclast.deployment/read-key pyroclast.deployment/id] :as config} aggregate-name {:keys [start end groups sort] :as query}]
  (let [promise (md/deferred)
        query (assoc query :datetime-format :iso-8601)]
    (http/get (deployment-aggregate-url config aggregate-name)
              {:async? true
               :throw-exceptions true
               :headers {"Content-type" "application/json"
                         "Authorization" read-key}
               :body (json/generate-string query)}
              (fn [{:keys [status body] :as resp}]
                (if (= 200 status)
                  (md/success! promise (json/parse-string body))
                  (common-response promise resp)))
              (partial md/error! promise))
    promise))

(defn deployment-fetch-aggregates
  "Returns the full aggregate for a deployment.
  Returns a dereffable deferred containing the full aggregate corresponding to
  the deployment specified in config."
  [{:keys [pyroclast.deployment/read-key pyroclast.deployment/id] :as config}]
  (let [promise (md/deferred)]
    (http/get (deployment-aggregates-url config)
              {:async? true
               :throw-exceptions true
               :headers {"Content-type" "application/json"
                         "Authorization" read-key}}
              (fn [{:keys [status body] :as resp}]
                (if (= 200 status)
                  (md/success! promise (json/parse-string body))
                  (common-response promise resp)))
              (partial md/error! promise))
    promise))

;; Unimplemented
#_(defn stream-topic!
    "Takes a Pyroclast API config and a consumer instance map as returned by
  `subscribe-to-topic`. Opens a SSE connection to the server, continously
  calling your callback with new messages."
    [{:keys [pyroclast.topic/read-key pyroclast.topic/id] :as config}
     {:keys [group-id consumer-instance-id] :as consumer-instance-map}]
    (assert group-id "Must supply a consumer instance map with a :group-id")
    (assert consumer-instance-id "Must supply a consumer instance map with a :consumer-instance-id")
    (let [{:keys [status body] :as response}
          (http/get (topic-poll-url config group-id consumer-instance-id)
                    {:throw-exceptions true
                     :as :stream
                     :headers {"Content-type" "text/event-stream"
                               "Authorization" read-key}})]
      (cond (= status 200) body
            (= status 400) (ex-info "Request was malformed." {:response response})
            (= status 401) (throw (ex-info "API key unauthorized to perform this action." {}))
            :else (throw (ex-info "Unknown problem. Open an issue on this repository if you're seeing this status." {})))))

#_(defn consumer-seek
    "Seek by offset or timestamp through a topic.
  Takes a Pyroclast API config map, a consumer instance map and a
  vector of directives for seeking the consumer instance on your topic.

  Each map entry in the vector specifies a partition and a seek strategy.
  `:partition` - can be either a string denoting the partition or `:all` for all partitions
  `:offset` - An ordinal offset representing a position on a specific or `:all` partitions

  Examples:
  `{:partition \"1\" :offset 12}` - seek to a specific offset.
  `{:partition \"0\" :timestamp \"1508293577\"}` - seek to a timestamp.
  `{:partition \"1\" :offset :beginning}` - seek to the beginning of the topics uncommited records.
  `{:partition \"1\" :offset :end}` - seek to the last uncommited record in the topic.
  `{:partition :all :offset :offset 12}` - seek all partitions to a specific offset.
  "
    [{:keys [pyroclast.topic/read-key pyroclast.topic/id] :as config}
     {:keys [group-id consumer-instance-id] :as consumer-instance-map}])
