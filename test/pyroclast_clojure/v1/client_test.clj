(ns pyroclast-clojure.v1.client-test
  (:require [pyroclast-clojure.v1.client :as client]
            [clojure.test :refer [is deftest]]))

(def config
  {:write-api-key "18c57dd4-dfe4-4460-826f-c1680435c979"
   :region "test"
   :topic-id "topic-0b7f355e-a5b4-4f9f-9887-5df6c65dec1e"
   :format :json})

(deftest producer-tests
  (is (= {:created true} (client/send-event! config {:event-type "page-visit" :page "/home" :timestamp 1495072835000})))

  (is (= '({:created true}
           {:created true}) 
         (client/send-events! config [{:event-type "page-visit" :page "/home" :timestamp 1495072835000}
                                      {:event-type "page-visit" :page "/console" :timestamp 1495072895032}])))

  (let [ret1 (promise)
        _ @(client/send-event-async!
            config (fn [result] (deliver ret1 result))
            {:event-type "page-visit" :page "store" :timestamp 1495072835000})
        ret2 (promise)
        _ @(client/send-events-async!
            config 
            (fn [results] (deliver ret2 results))
            [{:event-type "page-visit" :page "store" :timestamp 1495072835000}
             {:event-type "page-visit" :page "console" :timestamp 1495072895032}])]
    
    (is (= {:created true} @ret1))  
    (is (= '({:created true}
             {:created true}) 
           @ret2))))


; (def config
;   {:read-api-key "<token>"
;    :region "<region>"
;    :service-id "<service-id>"})

; (client/read-aggregates config)
; (client/read-aggregate config "aggregate-name")
; (client/read-aggregate-group config "aggregate-name" "group-name")
