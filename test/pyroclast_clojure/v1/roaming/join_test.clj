(ns pyroclast-clojure.v1.roaming.join-test
  (:require [clojure.test :refer :all]
            [pyroclast-clojure.v1.roaming.client :as roaming]
            [pyroclast-clojure.v1.roaming.join :as j]
            [pyroclast-clojure.v1.roaming.service :as s]
            [pyroclast-clojure.v1.roaming.topic :as t]
            [pyroclast-clojure.util :as u]))

(deftest ^:roaming test-join-by-key-static
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (j/join-by-key-static "joined" "id" [{"id" 1 "name" "Pyroclast"}
                                                         {"id" 2 "name" "Onyx"}])
                    (t/output-topic "output"))
        records [{"id" 1}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"id" 1 "joined" {"id" 1 "name" "Pyroclast"}}]
           (get-in simulation [:result :output-records])))))
