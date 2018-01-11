(ns pyroclast-clojure.v1.topic-test
  (:require [pyroclast-clojure.v1.client :as client]
            [clojure.test :refer [is deftest testing]]))


(deftest ^:topic topic-tests
  (let [config (:topic-client-config (read-string (slurp (clojure.java.io/resource "config.edn"))))]
    (let [group "my-subscriber-group"
          consumer-instance-map (client/topic-subscribe config group)]
      (println @(client/topic-consumer-information consumer-instance-map))
      (is @(client/topic-consumer-information consumer-instance-map))
      (testing "When we seek to the end we don't get any record back"
        (is (client/topic-consumer-seek-end consumer-instance-map))
        (is (zero? (count @(client/topic-consumer-poll! consumer-instance-map)))))
      (testing "We can add records and seek to the beginning, returning new records."
        (is @(client/topic-send-event! config {:value {:event-type "page-visit" :page "/home" :timestamp 1495072835000}}))
        (is @(client/topic-send-events! config [{:value {:event-type "page-visit" :page "/home" :timestamp 1495072835000}}
                                                {:value {:event-type "page-visit" :page "/console" :timestamp 1495072895032}}]))
        (is (client/topic-consumer-seek-beginning consumer-instance-map))
        (is (not (empty? @(client/topic-consumer-poll! consumer-instance-map)))))
      (testing "We can seek to end, commit, and open a new consumer on the same consumer-group seeing no new records."
        (is (client/topic-consumer-seek-end consumer-instance-map))
        (is (client/topic-consumer-commit-offsets consumer-instance-map))
        (is (zero? (count @(client/topic-consumer-poll! consumer-instance-map))))
        (let [new-consumer-instance-map (client/topic-subscribe config group)]
          (is (zero? (count @(client/topic-consumer-poll! new-consumer-instance-map)))))))))

;; Disable perf test for now as it messes with topic test's commit history
;; (deftest ^:performance prod-perf-tests
;;   (is true)
;;   (time
;;    (let [config (:topic-client-config (u/load-config "config.edn"))]
;;      (dotimes [r 50]
;;        (run! deref (mapv (fn [i]
;;                            (client/topic-send-events!
;;                             config
;;                             [{:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}]))
;;                          (range 50)))))))
