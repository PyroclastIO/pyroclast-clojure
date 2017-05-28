(ns pyroclast-clojure.v1.client
  (:require [clojure.string :refer [join]]
            [clojure.spec.alpha :as s]
            [clojure.tools.logging :as log]
            [clj-http.client :as client]
            [cheshire.core :refer [generate-string parse-string]]))

(def default-region "us-east-1")

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
        {:created false :reason "Unknown" :response response}))

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

(defn process-service-response [{:keys [status body] :as response}]
  (cond (= status 200)
        {:success? true
         :aggregates (parse-string body true)}

        (= status 401)
        {:success? false :reason "API key unauthorized to perform this action."}

        (= status 404)
        {:success? false :reason "Aggregate not found"}

        :else
        {:success? false :reason "Unknown" :response response}))

(defn read-aggregates [{:keys [read-api-key service-id] :as config}]
  (let [response
        (client/get (format "%s/v1/service/%s" (base-url config) service-id)
                    {:headers {"Authorization" read-api-key}
                     :accept :json
                     :throw-exceptions? false})]
    (process-service-response response)))

(defn read-aggregate [{:keys [read-api-key service-id :as config]} aggregate-name]
  (let [response
        (client/get (format "%s/v1/service/%s/aggregates/%s" (base-url config) service-id aggregate-name)
                    {:headers {"Authorization" read-api-key}
                     :accept :json
                     :throw-exceptions? false})]
    (process-service-response response)))

(defn read-aggregate-group [{:keys [read-api-key service-id :as config]} aggregate-name group-name]
  (let [response
        (client/get (format "%s/v1/service/%s/aggregates/%s/group/%s" (base-url config) service-id aggregate-name group-name)
                    {:headers {"Authorization" read-api-key}
                     :accept :json
                     :throw-exceptions? false})]
    (process-service-response response)))
