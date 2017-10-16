(ns pyroclast-clojure.v1.client
  (:require [clojure.string :refer [join]]
            [clojure.tools.logging :as log]
            [clj-http.client :as client]
            [cheshire.core :refer [generate-string parse-string]]))

(def default-region "us-west-2")

(def unknown-message "Unknown problem. Open an issue on this repository if you're seeing this status.")

(defmulti format-payload
  (fn [fmt payload]
    fmt))

(defmethod format-payload :json
  [fmt payload]
  (generate-string payload))

(defmethod format-payload :none
  [fmt payload]
  payload)

(defmulti content-type
  (fn [fmt] fmt))

(defmethod content-type :json
  [fmt] "application/json")

(defmethod content-type :none
  [fmt] "text/plain")

(defn process-topic-response [{:keys [status body] :as response}]
  (cond (= status 200)
        (parse-string body true)

        (= status 400)
        {:created false :reason "Event data was malformed."}

        (= status 401)
        {:created false :reason "API key unauthorized to perform this action."}

        :else
        {:created false :reason unknown-message :response response}))

(defn base-url [{:keys [region endpoint] :or {region default-region} :as config}]
  (if endpoint
    endpoint
    (str (format "https://api.%s.pyroclast.io" region))))

(defn process-exception [e]
  (log/warn e "This function should never be invoked. Open an issue on this library if you see this."))

(defn validate-event! [event]
  (when-not (contains? event :value)
    (throw (ex-info "Event requires :value key" {:event event}))))

(defn send-event! [{:keys [write-api-key topic-id] :as config} event]
  (validate-event! event)
  (let [response
        (client/post (format "%s/v1/topics/%s/produce" (base-url config) topic-id)
                     {:headers {"Authorization" write-api-key
                                "Content-type" (content-type (:format config))}
                      :body (format-payload (:format config) event)
                      :accept :json})]
    (process-topic-response response)))

(defn send-events! [{:keys [write-api-key topic-id] :as config} events]
  (run! validate-event! events)
  (let [response
        (client/post (format "%s/v1/topics/%s/bulk-produce" (base-url config) topic-id)
                     {:headers {"Authorization" write-api-key
                                "Content-type" (content-type (:format config))}
                      :body (format-payload (:format config) events)
                      :accept :json})]
    (process-topic-response response)))

(defn send-event-async! [{:keys [write-api-key topic-id] :as config} callback event]
  (validate-event! event)
  (client/post (format "%s/v1/topics/%s/produce" (base-url config) topic-id)
               {:async? true
                :throw-exceptions false
                :headers {"Authorization" write-api-key
                          "Content-type" (content-type (:format config))}
                :body (format-payload (:format config) event)
                :accept :json}
               (fn [response] (callback (process-topic-response response)))
               (fn [e] (process-exception e))))

(defn send-events-async! [{:keys [write-api-key topic-id] :as config} callback events]
  (run! validate-event! events)
  (client/post (format "%s/v1/topics/%s/bulk-produce" (base-url config) topic-id)
               {:async? true
                :throw-exceptions false
                :headers {"Authorization" write-api-key
                          "Content-type" (content-type (:format config))}
                :body (format-payload (:format config) events)
                :accept :json}
               (fn [response] (callback (process-topic-response response)))
               (fn [e] (process-exception e))))

(defn subscribe-to-topic!
  "Creates a new consumer, joining it to the specified consumer group. Returns a
  consumer instance map.

  offset - One of `:earliest` or `:latest`
  partitions - A list of partitions to consumer from. `:all` will consume from all partitions"
  [{:keys [read-api-key topic-id] :as config} consumer-group-name {:keys [offset partitions]
                                                                   :or {offset :earliest
                                                                        partitions :all}}]
  (when (not (re-matches #"[a-zA-Z0-9-_]+" consumer-group-name))
    (throw (ex-info "Consumer group name must be a non-empty string of alphanumeric characters." {})))
  (assert (#{:earliest :latest} offset))
  (let [response
        (client/post (format "%s/v1/topics/%s/consumers/%s/subscribe" (base-url config) topic-id consumer-group-name)
                     {:headers {"Authorization" read-api-key}
                      :accept :json
                      :content-type :json
                      :body (generate-string {"offset" offset
                                              "partitions" partitions})
                      :throw-exceptions? false})
        {:keys [status body]} response]
    (cond (= status 201)
          (parse-string body true)

          (= status 401)
          (ex-info "API key unauthorized to perform this action." {})

          :else
          (ex-info unknown-message {:response response}))))

(defn poll-topic!
  "Polls a topic using a consumer instance map."
  [{:keys [read-api-key topic-id] :as config} {:keys [group-id consumer-instance-id] :as consumer-instance-map}]
  (let [response
        (client/post (format "%s/v1/topics/%s/consumers/%s/instances/%s/poll" (base-url config) topic-id group-id consumer-instance-id)
                     {:headers {"Authorization" read-api-key}
                      :accept :json
                      :throw-exceptions? false})
        {:keys [status body]} response]
    (cond (= status 200)
          (parse-string body true)

          (= status 401)
          (ex-info "API key unauthorized to perform this action." {})

          :else
          (ex-info unknown-message {:response response}))))

(defn commit-read-records! [{:keys [read-api-key topic-id] :as config} {:keys [group-id consumer-instance-id] :as consumer-instance-map}]
  (let [response
        (client/post (format "%s/v1/topics/%s/consumers/%s/instances/%s/commit" (base-url config) topic-id group-id consumer-instance-id)
                     {:headers {"Authorization" read-api-key}
                      :accept :json
                      :throw-exceptions? false})
        {:keys [status body]} response]
    (cond (= status 200) true

          (= status 401)
          (ex-info "API key unauthorized to perform this action." {})

          :else
          (ex-info unknown-message {:response response}))))

(defn process-service-response [{:keys [status body] :as response}]
  (cond (= status 200)
        {:success? true
         :aggregates (parse-string body true)}

        (= status 401)
        {:success? false :reason "API key unauthorized to perform this action."}

        (= status 404)
        {:success? false :reason "Aggregate not found"}

        :else
        {:success? false :reason unknown-message :response response}))

(defn read-aggregates [{:keys [read-api-key deployment-id] :as config}]
  (let [response
        (client/get (format "%s/v1/deployments/%s/aggregates" (base-url config) deployment-id)
                    {:headers {"Authorization" read-api-key}
                     :accept :json
                     :throw-exceptions? false})]
    (process-service-response response)))

(defn make-query-criteria [{:keys [start end groups datetime-format sort?]}]
  (when groups
    (when (not (seq groups))
      (throw (ex-info "Groups must be a non-empty collection." {:groups groups}))))
  (cond-> {}
    start (assoc :start start)
    end (assoc :end end)
    groups (assoc :groups groups)
    sort? (assoc :sort sort?)
    datetime-format (assoc :datetime-format datetime-format)))

(defn read-aggregate
  ([config aggregate-name]
   (read-aggregate config aggregate-name {}))
  ([{:keys [read-api-key deployment-id] :as config} aggregate-name opts]
   (let [response
         (client/post (format "%s/v1/deployments/%s/aggregates/%s" (base-url config) deployment-id aggregate-name)
                      {:headers {"Authorization" read-api-key}
                       :body (generate-string (make-query-criteria opts))
                       :accept :json
                       :throw-exceptions? false})]
     (process-service-response response))))
