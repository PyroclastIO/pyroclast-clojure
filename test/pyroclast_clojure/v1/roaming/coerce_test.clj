(ns pyroclast-clojure.v1.roaming.coerce-test
  (:require [clojure.test :refer :all]
            [pyroclast-clojure.v1.roaming.client :as roaming]
            [pyroclast-clojure.v1.roaming.coerce :as coerce]
            [pyroclast-clojure.v1.roaming.service :as s]
            [pyroclast-clojure.v1.roaming.topic :as t]
            [pyroclast-clojure.util :as u]))

(deftest ^:roaming test-parse-vals
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (coerce/parse-vals {"x" "long"})
                    (t/output-topic "output"))
        records [{"x" "45"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"x" 45}]
           (get-in simulation [:result :output-records])))))
