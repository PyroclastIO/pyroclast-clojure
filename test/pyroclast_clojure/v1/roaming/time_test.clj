(ns pyroclast-clojure.v1.roaming.time-test
  (:require [clojure.test :refer :all]
            [pyroclast-clojure.v1.roaming.client :as roaming]
            [pyroclast-clojure.v1.roaming.time :as time]
            [pyroclast-clojure.v1.roaming.service :as s]
            [pyroclast-clojure.v1.roaming.topic :as t]
            [pyroclast-clojure.util :as u]))

(deftest ^:roaming test-parse-datetime
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (time/parse-datetime "timestamp" "YYYY-MM-dd'T'HH:mm:ss")
                    (t/output-topic "output"))
        records [{"timestamp" "2017-07-06T07:03:02"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"timestamp" 1499324582000}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-format-unix-ms-timestamp
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (time/format-unix-ms-timestamp "timestamp" "YYYY-MM-dd'T'HH:mm:ss")
                    (t/output-topic "output"))
        records [{"timestamp" 1499324582000}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"timestamp" "2017-07-06T07:03:02"}]
           (get-in simulation [:result :output-records])))))