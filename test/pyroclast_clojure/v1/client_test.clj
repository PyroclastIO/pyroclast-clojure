(ns pyroclast-clojure.v1.client-test
  (:require [pyroclast-clojure.v1.client :as client]
            [pyroclast-clojure.util :as u]
            [clojure.test :refer [is deftest]]))

(deftest ^:topic producer-tests
  (let [config (:topic (u/load-config "config.edn"))]
    (is (= {:created true} (client/send-event! config {:value {:event-type "page-visit" :page "/home" :timestamp 1495072835000}})))

    (is (= {:created true}
           (client/send-events! config [{:value {:event-type "page-visit" :page "/home" :timestamp 1495072835000}}
                                        {:value {:event-type "page-visit" :page "/console" :timestamp 1495072895032}}])))

    (let [ret1 (promise)
          _ @(client/send-event-async!
              config (fn [result] (deliver ret1 result))
              {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}})
          ret2 (promise)
          _ @(client/send-events-async!
              config 
              (fn [results] (deliver ret2 results))
              [{:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
               {:value {:event-type "page-visit" :page "console" :timestamp 1495072895032}}])]
      
      (is (= {:created true} @ret1))  
      (is (= {:created true}
             @ret2)))

    (let [group "my-subscriber-group"
          sub-response (client/subscribe-to-topic! config group)
          poll-response (client/poll-topic! config group)
          commit-response (client/commit-read-records! config group)]
      (is (:success? sub-response))
      (is (:success? poll-response))
      (is (= 6 (count (:records poll-response))))
      (is (:success? commit-response)))))

(deftest ^:performance producer-tests
  (time 
   (let [config (:topic (u/load-config "config.edn"))] 
     (dotimes [r 50]
       (run! deref (pmap (fn [i] 
                           (let [ret2 (promise)] 
                             (client/send-events-async!
                              config 
                              (fn [results] (deliver ret2 results))
                              [{:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                               {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                               {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                               {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                               {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                               {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                               {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                               {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                               {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}
                               {:value {:event-type "page-visit" :page "store" :timestamp 1495072835000}}])
                             ret2))
                         (range 50)))))))
