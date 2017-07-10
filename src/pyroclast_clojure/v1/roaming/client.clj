(ns pyroclast-clojure.v1.roaming.client
  (:require [clojure.walk :as w]
            [clj-http.client :as client]
            [cheshire.core :refer [generate-string parse-string]]))

(defn process-simulation-response [{:keys [status body] :as response}]
  (cond (= status 200)
        {:success? true
         :result (-> body
                     (update-in [:output-records] w/stringify-keys)
                     (update-in [:aggregates] w/stringify-keys))}

        (= status 400)
        (assoc (parse-string body false) :success? false)

        :else
        {:success? false :reason "Encountered an internal error." :response response}))

(defn simulate! [{:keys [endpoint] :as config} service records]
  (let [response
        (client/post (format "%s/v1/roaming/simulate" endpoint)
                     {:headers {"Content-type" "application/json"}
                      :body (generate-string {:pyroclast/roaming-service service
                                              :roaming/records records})
                      :as :json
                      :accept :json
                      :throw-exceptions? false})]
    (process-simulation-response response)))
