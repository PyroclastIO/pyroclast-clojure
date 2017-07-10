(ns pyroclast-clojure.v1.roaming.coerce-test
  (:require [clojure.test :refer :all]
            [pyroclast-clojure.v1.roaming.client :as roaming]
            [pyroclast-clojure.v1.roaming.coerce :as coerce]
            [pyroclast-clojure.v1.roaming.service :as s]
            [pyroclast-clojure.v1.roaming.topic :as t]))

(def config {:endpoint "http://localhost:10557"})

(deftest test-parse-vals
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (coerce/parse-vals {"x" "long"})
                    (t/output-topic "output"))
        records [{"x" "45"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"x" 45}]
           (get-in simulation [:result :output-records])))))
