(ns pyroclast-clojure.v1.topic-test
  (:require [pyroclast-clojure.v1.client :as client]
            [pyroclast-clojure.util :as u]
            [clojure.test :refer [is deftest]]))

(deftest ^:topic topic-tests
  (let [admin-config (:admin-client-config (u/load-config "config.edn"))]
    (let [new-topic-cfg @(client/create-topic! admin-config (str (java.util.UUID/randomUUID)))
          group "my-subscriber-group"
          consumer-instance-map (client/topic-subscribe new-topic-cfg group)
          ]
      (client/topic-consumer-seek-end consumer-instance-map)
      (is @(client/topic-send-event! new-topic-cfg {:value {:event-type "page-visit" :page "/home" :timestamp 1495072835000}}))
      (is @(client/topic-send-events! new-topic-cfg [{:value {:event-type "page-visit" :page "/home" :timestamp 1495072835000}}
                                              {:value {:event-type "page-visit" :page "/console" :timestamp 1495072895032}}]))
      (Thread/sleep 1000)
      (is (pos? (count @(client/topic-consumer-poll! consumer-instance-map))))
      (is (client/topic-consumer-commit-offsets consumer-instance-map))
      (is (empty? @(client/topic-consumer-poll! consumer-instance-map)))
      (client/topic-consumer-seek-beginning consumer-instance-map)
      (is (not (empty? @(client/topic-consumer-poll! consumer-instance-map)))))))

;; Disable perf test for now as it messes with topic test's commit history
; (deftest ^:performance prod-perf-tests
;   (is true)
;   (time
;    (let [config (:topic-client-config (u/load-config "config.edn"))]
;      (dotimes [r 50]
;        (run! deref (mapv (fn [i]
;                            (client/topic-send-events!
;                             config
;                             [{:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
;                              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}]))
;                          (range 50)))))))
