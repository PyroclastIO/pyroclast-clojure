(ns pyroclast-clojure.roaming.join-test
  (:require [clojure.test :refer :all]
            [pyroclast-clojure.roaming.client :as roaming]
            [pyroclast-clojure.roaming.join :as j]
            [pyroclast-clojure.roaming.service :as s]
            [pyroclast-clojure.roaming.topic :as t]))

(def config {:endpoint "http://localhost:10557"})

(deftest test-join-by-key-static
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (j/join-by-key-static "joined" "id" [{"id" 1 "name" "Pyroclast"}
                                                         {"id" 2 "name" "Onyx"}])
                    (t/output-topic "output"))
        records [{"id" 1}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"id" 1 "joined" {"id" 1 "name" "Pyroclast"}}]
           (get-in simulation [:result :output-records])))))
