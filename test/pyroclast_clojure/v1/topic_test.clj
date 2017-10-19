(ns pyroclast-clojure.v1.topic-test
  (:require [pyroclast-clojure.v1.client :as client]
            [pyroclast-clojure.util :as u]
            [clojure.test :refer [is deftest]]))

(deftest ^:topic producer-tests
  (let [config (:client-config (u/load-config "config.edn"))]
    (is @(client/topic-send-event! config {:value {:event-type "page-visit" :page "/home" :timestamp 1495072835000}}))

    (is @(client/topic-send-events! config [{:value {:event-type "page-visit" :page "/home" :timestamp 1495072835000}}
                                            {:value {:event-type "page-visit" :page "/console" :timestamp 1495072895032}}]))

    (let [group "my-subscriber-group"
          consumer-instance-map @(client/topic-subscribe config group)]
      (is (pos? (count @(client/topic-consumer-poll! config consumer-instance-map))))
      (is (client/topic-consumer-commit-offsets config consumer-instance-map))
      (is (empty? @(client/topic-consumer-poll! config consumer-instance-map))))))

(deftest ^:performance producer-tests
  (is true)
  (time
   (let [config (:client-config (u/load-config "config.edn"))]
     (dotimes [r 50]
       (run! deref (mapv (fn [i]
                           (client/topic-send-events!
                            config
                            [{:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                             {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                             {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                             {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                             {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                             {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                             {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                             {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                             {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                             {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}]))
                         (range 50)))))))
