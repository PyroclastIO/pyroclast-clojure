(ns pyroclast-clojure.v1.client
  (:require [clojure.string :refer [join]]
            [clj-http.client :as client]
            [cheshire.core :refer [generate-string parse-string]]))

(defmulti format-event
  (fn [fmt event]
    fmt))

(defmethod format-event :json
  [fmt event]
  (generate-string event))

(defmethod format-event :none
  [fmt event]
  event)

(defmulti content-type
  (fn [fmt] fmt))

(defmethod content-type :json
  [fmt] "application/json")

(defmethod content-type :none
  [fmt] "text/plain")

(defn send-event! [{:keys [write-api-key endpoint topic-id] :as config} event]
  (let [result
        (client/post (format "%s/api/v1/topic/%s/events" endpoint topic-id)
                     {:headers {"Authorization" write-api-key
                                "Content-type" (content-type (:format config))}
                      :body (format-event (:format config) event)
                      :accept :json})]
    (if (= (:status result 200))
      (parse-string (:body result) true)
      result)))
