(ns pyroclast-clojure.v1.roaming.topic
  (:require [pyroclast-clojure.util :as u]))

(defn input-topic [service topic-id]
  (u/integrate-params service :input/kafka-reader 0
                      {:input.kafka-reader/topic topic-id}))

(defn output-topic [service topic-id]
  (u/integrate-params service :output/kafka-writer 0
                      {:output.kafka-writer/topic topic-id}))
