(ns pyroclast-clojure.v1.client
  (:require [clojure.string :refer [join]]
            [clojure.spec.alpha :as s]
            [clojure.tools.logging :as log]
            [clj-http.client :as client]
            [cheshire.core :refer [generate-string parse-string]]))

(def default-region "us-east-1")

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

(defn send-event! [{:keys [write-api-key topic-id] :as config} event]
  (let [response
        (client/post (format "%s/v1/topics/%s/produce" (base-url config) topic-id)
                     {:headers {"Authorization" write-api-key
                                "Content-type" (content-type (:format config))}
                      :body (format-payload (:format config) event)
                      :accept :json})]
    (process-topic-response response)))

(defn send-events! [{:keys [write-api-key topic-id] :as config} events]
  (let [response
        (client/post (format "%s/v1/topics/%s/bulk-produce" (base-url config) topic-id)
                     {:headers {"Authorization" write-api-key
                                "Content-type" (content-type (:format config))}
                      :body (format-payload (:format config) events)
                      :accept :json})]
    (process-topic-response response)))

(defn send-event-async! [{:keys [write-api-key topic-id] :as config} callback event]
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
  (client/post (format "%s/v1/topics/%s/bulk-produce" (base-url config) topic-id)
               {:async? true
                :throw-exceptions false
                :headers {"Authorization" write-api-key
                          "Content-type" (content-type (:format config))}
                :body (format-payload (:format config) events)
                :accept :json}
               (fn [response] (callback (process-topic-response response)))
               (fn [e] (process-exception e))))

(defn process-subscribe-response [{:keys [status body] :as response}]
  (cond (= status 200)
        {:success? true
         :group-id (:group-id (parse-string body true))}

        (= status 401)
        {:success? false :reason "API key unauthorized to perform this action."}

        :else
        {:success? false :reason unknown-message :response response}))

(defn subscribe-to-topic! [{:keys [read-api-key topic-id] :as config} subscriber-name]
  (when (not (re-matches #"[a-zA-Z0-9-_]+" subscriber-name))
    (throw (ex-info "Subscriber name must be a non-empty string of alphanumeric characters." {})))
  (let [response
        (client/post (format "%s/v1/topics/%s/subscribe/%s" (base-url config) topic-id subscriber-name)
                     {:headers {"Authorization" read-api-key}
                      :accept :json
                      :throw-exceptions? false})]
    (process-subscribe-response response)))

(defn process-poll-response [{:keys [status body] :as response}]
  (cond (= status 200)
        {:success? true
         :records (parse-string body true)}

        (= status 401)
        {:success? false :reason "API key unauthorized to perform this action."}

        :else
        {:success? false :reason unknown-message :response response}))

(defn poll-topic! [{:keys [read-api-key topic-id] :as config} subscriber-name]
  (let [response
        (client/post (format "%s/v1/topics/%s/poll/%s" (base-url config) topic-id subscriber-name)
                     {:headers {"Authorization" read-api-key}
                      :accept :json
                      :throw-exceptions? false})]
    (process-poll-response response)))

(defn process-commit-read-response [{:keys [status body] :as response}]
  (cond (= status 200)
        {:success? true}

        (= status 401)
        {:success? false :reason "API key unauthorized to perform this action."}

        :else
        {:success? false :reason unknown-message :response response}))

(defn commit-read-records! [{:keys [read-api-key topic-id] :as config} subscriber-name]
  (let [response
        (client/post (format "%s/v1/topics/%s/poll/%s/commit" (base-url config) topic-id subscriber-name)
                     {:headers {"Authorization" read-api-key}
                      :accept :json
                      :throw-exceptions? false})]
    (process-commit-read-response response)))

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

(defn read-aggregates [{:keys [read-api-key service-id] :as config}]
  (let [response
        (client/get (format "%s/v1/services/%s" (base-url config) service-id)
                    {:headers {"Authorization" read-api-key}
                     :accept :json
                     :throw-exceptions? false})]
    (process-service-response response)))

(defn read-aggregate [{:keys [read-api-key service-id :as config]} aggregate-name]
  (let [response
        (client/get (format "%s/v1/services/%s/aggregates/%s" (base-url config) service-id aggregate-name)
                    {:headers {"Authorization" read-api-key}
                     :accept :json
                     :throw-exceptions? false})]
    (process-service-response response)))

(defn read-aggregate-group [{:keys [read-api-key service-id :as config]} aggregate-name group-name]
  (let [response
        (client/get (format "%s/v1/services/%s/aggregates/%s/group/%s" (base-url config) service-id aggregate-name group-name)
                    {:headers {"Authorization" read-api-key}
                     :accept :json
                     :throw-exceptions? false})]
    (process-service-response response)))
