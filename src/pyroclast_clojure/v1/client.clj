(ns pyroclast-clojure.v1.client
  (:require [clojure.string :refer [join]]
            [clojure.spec.alpha :as s]
            [clj-http.client :as client]
            [cheshire.core :refer [generate-string parse-string]]))

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

(defn send-request! [endpoint api-key fmt topic-id formatted-payload]
  (client/post (format "%s/api/v1/topic/%s/events" endpoint topic-id)
               {:headers {"Authorization" api-key
                          "Content-type" (content-type fmt)}
                :body formatted-payload
                :accept :json}))

(defn send-event! [{:keys [write-api-key endpoint topic-id] :as config} event]
  (let [payload (format-payload (:format config) event)
        result (send-request! endpoint write-api-key (:format config) topic-id payload)]
    (if (= (:status result 200))
      (parse-string (:body result) true)
      result)))

(defn send-events! [{:keys [write-api-key endpoint topic-id] :as config} events]
  (let [payload (format-payload (:format config) events)
        result (send-request! endpoint write-api-key (:format config) topic-id payload)]
    (if (= (:status result 200))
      (parse-string (:body result) true)
      result)))
