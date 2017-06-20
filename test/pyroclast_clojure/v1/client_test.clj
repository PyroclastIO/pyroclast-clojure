(ns pyroclast-clojure.v1.client-test
  (:require [pyroclast-clojure.v1.client :as client]
            [clojure.test :refer [is deftest]]))

(def config
  {:read-api-key "26ee27ed-8d6c-481f-894c-6e2b219c2865"
   :write-api-key "0e0efdd3-293e-4ef7-995d-543d17c65d26"
   :topic-id "topic-a0e4227b-6536-44e5-b68d-ac5a054ce2e7"
   :endpoint "http://localhost:10556"
   :format :json})

(deftest producer-tests
  (is (= {:created true} (client/send-event! config {:value {:event-type "page-visit" :page "/home" :timestamp 1495072835000}})))

  (is (= '({:created true}
           {:created true}) 
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
    (is (= '({:created true}
             {:created true}) 
           @ret2)))

  (let [group "my-subscriber-group"
        sub-response (client/subscribe-to-topic! config group)
        poll-response (client/poll-topic! config group)
        commit-response (client/commit-read-records! config group)]
    (is (:success? sub-response))
    (is (:success? poll-response))
    (is (= 6 (count (:records poll-response))))
    (is (:success? commit-response))))


(comment (client/read-aggregates config)
         (client/read-aggregate config "group-by-vals")
         (client/read-aggregate-group config "aggregate-name" "group-name"))
