(ns pyroclast-clojure.v1.roaming.compound
  (:require [clojure.test :refer :all]
            [pyroclast-clojure.v1.roaming.client :as roaming]
            [pyroclast-clojure.v1.roaming.seq :as pseq]
            [pyroclast-clojure.v1.roaming.string :as string]
            [pyroclast-clojure.v1.roaming.math :as math]
            [pyroclast-clojure.v1.roaming.service :as s]
            [pyroclast-clojure.v1.roaming.filters :as f]
            [pyroclast-clojure.v1.roaming.aggregations :as a]
            [pyroclast-clojure.v1.roaming.coerce :as c]
            [pyroclast-clojure.v1.roaming.topic :as t]))

(def config {:endpoint "http://localhost:10557"})

(deftest test-chained-functions
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/split-whitespace "sentence")
                    (pseq/explode "sentence" {:dst "word"})
                    (string/replace "word" "[^a-zA-Z0-0]" "")
                    (string/lower-case "word")
                    (t/output-topic "output"))
        records [{"sentence" "Pyroclast Roaming is a tool virtualizes Pyroclast's runtime."}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"word" "pyroclast"}
            {"word" "roaming"}
            {"word" "is"}
            {"word" "a"}
            {"word" "tool"}
            {"word" "virtualizes"}
            {"word" "pyroclasts"}
            {"word" "runtime"}]
           (get-in simulation [:result :output-records])))))

(def temperature-records
  [{"sensor-id" "1" "event-type" "reading" "value" "50.24" "unit" "fahrenheit"}
   {"sensor-id" "2" "event-type" "reading" "value" "48.37" "unit" "fahrenheit"}
   {"sensor-id" "3" "event-type" "reading" "value" "52.98" "unit" "fahrenheit"}
   {"sensor-id" "2" "event-type" "ping"}
   {"sensor-id" "1" "event-type" "reading" "value" "49.53" "unit" "fahrenheit"}
   {"sensor-id" "3" "event-type" "reading" "value" "51.13" "unit" "fahrenheit"}
   {"sensor-id" "2" "event-type" "reading" "value" "50.03" "unit" "fahrenheit"}
   {"sensor-id" "1" "event-type" "ping"}
   {"sensor-id" "2" "event-type" "reading" "value" "50.01" "unit" "fahrenheit"}])

(deftest temperature-sensors-by-id
  (let [service (-> (s/new-service)
                    (t/input-topic "sensor-events")
                    (f/= "event-type" "reading")
                    (c/parse-vals {"value" "double"})
                    (math/minus "value" 32)
                    (math/divide "value" 1.8)
                    (pseq/assoc-in ["unit"] "celsius")
                    (math/round-decimals "value" 2)
                    (a/aggregate-together
                     [(a/min "min-reading" "value" (a/globally-windowed))
                      (a/max "max-reading" "value" (a/globally-windowed))
                      (a/average "avg-reading" "value" (a/globally-windowed))]
                     "sensor-id"))

        simulation (roaming/simulate! config service temperature-records)

        min-reading (get-in simulation [:result :aggregates "min-reading"])
        max-reading (get-in simulation [:result :aggregates "max-reading"])
        avg-reading (get-in simulation [:result :aggregates "avg-reading"])]

    (is (:success? simulation) simulation)

    (is (= {"1" {"value" 9.74}
            "2" {"value" 9.09}
            "3" {"value" 10.63}}
           min-reading))

    (is (= {"1" {"value" 10.13}
            "2" {"value" 10.02}
            "3" {"value" 11.66}}
           max-reading))

    (is (= {"1" {"value" 9.935}
            "2" {"value" 9.706666666666665}
            "3" {"value" 11.145}}
           avg-reading))))
