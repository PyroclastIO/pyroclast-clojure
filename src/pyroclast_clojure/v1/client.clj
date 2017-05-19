(ns pyroclast-clojure.v1.client
  (:require [clojure.string :refer [join]]
            [clojure.spec.alpha :as s]
            [clj-http.client :as client]
            [cheshire.core :refer [generate-string parse-string]]
            [taoensso.timbre :refer [warn]]))

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

(defn process-response [{:keys [status body] :as response}]
  (cond (= status 200)
        (parse-string body true)

        (= status 400)
        {:created false :reason "Event data was malformed."}

        (= status 401)
        {:created false :reason "API key unauthorized to perform this action."}

        :else
        {:created false :reason "Unknown" :response response}))

(defn send-synchronous-request! [endpoint api-key fmt topic-id formatted-payload]
  (client/post (format "%s/api/v1/topic/%s/events" endpoint topic-id)
               {:headers {"Authorization" api-key
                          "Content-type" (content-type fmt)}
                :body formatted-payload
                :accept :json}))

(defn process-exception [e]
  (warn e "This function should never be invoked. Open an issue on this library if you see this."))

(defn send-event! [{:keys [write-api-key endpoint topic-id] :as config} event]
  (let [payload (format-payload (:format config) event)
        response (send-synchronous-request! endpoint write-api-key (:format config) topic-id payload)]
    (process-response response)))

(defn send-events! [{:keys [write-api-key endpoint topic-id] :as config} events]
  (let [payload (format-payload (:format config) events)
        response (send-synchronous-request! endpoint write-api-key (:format config) topic-id payload)]
    (process-response response)))

(defn send-event-async! [{:keys [write-api-key endpoint topic-id] :as config} callback event]
  (client/post (format "%s/api/v1/topic/%s/events" endpoint topic-id)
               {:async? true
                :throw-exceptions false
                :headers {"Authorization" write-api-key
                          "Content-type" (content-type (:format config))}
                :body (format-payload (:format config) event)
                :accept :json}
               (fn [response] (callback (process-response response)))
               (fn [e] (process-exception e))))

(defn send-events-async! [{:keys [write-api-key endpoint topic-id] :as config} callback events]
  (client/post (format "%s/api/v1/topic/%s/events" endpoint topic-id)
               {:async? true
                :throw-exceptions false
                :headers {"Authorization" write-api-key
                          "Content-type" (content-type (:format config))}
                :body (format-payload (:format config) events)
                :accept :json}
               (fn [response] (callback (process-response response)))
               (fn [e] (process-exception e))))
