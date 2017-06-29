(ns pyroclast-clojure.v1.client-test
  (:require [pyroclast-clojure.v1.client :as client]
            [clojure.test :refer [is deftest]]))

(def config
  {:read-api-key "8e0dfbee-eede-4408-b0d2-ba4bde27c9f6" 
   :write-api-key "38afdad0-0598-415b-8a08-9b1b5efc0a3e"
   :topic-id "topic-6856aae4-ba19-4d57-9949-5d35a5d5caba"
   :endpoint "http://localhost:10556"
   :format :json})

(defn bench [] 
  (time 
   (let [batch (mapv (fn [v]
                       {:value {:my :value}})
                     (range 100))] 
     (dotimes [n-iters 100]
       (let [n-requests 100
             n-threads 1
             n-sent (atom 0)] 
         (pmap (fn [r] 
                 (dotimes [i (int (/ n-requests n-threads))]
                   (client/send-events-async!
                    config 
                    (fn [result] 
                      (swap! n-sent inc))
                    batch)))
               (range n-threads))
         (loop []
           (when (not= @n-sent n-requests)
             (Thread/sleep 100)
             (recur))))))))

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
