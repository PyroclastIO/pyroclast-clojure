(ns pyroclast-clojure.client
  (:require [clojure.string :refer [join]]
            [clj-http.client :as client]
            [cheshire.core :refer [generate-string]]))

(defmulti format-event
  (fn [fmt event]
    fmt))

(defmethod format-event :json
  [fmt event]
  (generate-string event))

(defmethod format-event :none
  [fmt event]
  event)

(defn send-event! [{:keys [user-token api-token endpoint] :as config} topic-id event fmt]
  (client/post (format "%s?topic-id=%s" endpoint topic-id)
               {:basic-auth [user-token api-token]
                :body (format-event fmt event)
                :content-type fmt
                :accept :json}))
