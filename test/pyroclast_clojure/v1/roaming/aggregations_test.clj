(ns pyroclast-clojure.v1.roaming.aggregations-test
  (:require [clojure.test :refer :all]
            [pyroclast-clojure.v1.roaming.client :as roaming]
            [pyroclast-clojure.v1.roaming.aggregations :as a]
            [pyroclast-clojure.v1.roaming.service :as s]
            [pyroclast-clojure.v1.roaming.topic :as t]))

(def config {:endpoint "http://localhost:10557"})

(def records
  [{"timestamp" 1499465513000 "nation" "usa" "team" "red" "points" 20}
   {"timestamp" 1499465413042 "nation" "usa" "team" "blue" "points" 5}
   {"timestamp" 1499465513040 "nation" "canada" "team" "red" "points" 10}
   {"timestamp" 1499445513000 "nation" "mexico" "team" "green" "points" 20}
   {"timestamp" 1499462511000 "nation" "usa" "team" "red" "points" 15}
   {"timestamp" 1499463213000 "nation" "canada" "team" "orange" "points" 50}
   {"timestamp" 1499325313000 "nation" "canada" "team" "orange" "points" 7}])

(deftest test-count
  (let [service (-> (s/new-service)
                    (t/input-topic "events")
                    (a/aggregate-together
                     [(a/count "hourly-page-views" (a/fixed-windows-of 1 "hour" "timestamp"))]))
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"bounds" {"lower" 1499464800000 "upper" 1499468399999} "value" 3}
            {"bounds" {"lower" 1499443200000 "upper" 1499446799999} "value" 1}
            {"bounds" {"lower" 1499461200000 "upper" 1499464799999} "value" 2}
            {"bounds" {"lower" 1499324400000 "upper" 1499327999999} "value" 1}]
           (get-in simulation [:result :aggregates "hourly-page-views"])))))

(deftest test-min
  (let [service (-> (s/new-service)
                    (t/input-topic "events")
                    (a/aggregate-together
                     [(a/min "hourly-page-views" "points" (a/fixed-windows-of 1 "hour" "timestamp"))]))
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"bounds" {"lower" 1499464800000 "upper" 1499468399999} "value" 5}
            {"bounds" {"lower" 1499443200000 "upper" 1499446799999} "value" 20}
            {"bounds" {"lower" 1499461200000 "upper" 1499464799999} "value" 15}
            {"bounds" {"lower" 1499324400000 "upper" 1499327999999} "value" 7}]
           (get-in simulation [:result :aggregates "hourly-page-views"])))))

(deftest test-max
  (let [service (-> (s/new-service)
                    (t/input-topic "events")
                    (a/aggregate-together
                     [(a/max "hourly-page-views" "points" (a/fixed-windows-of 1 "hour" "timestamp"))]))
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"bounds" {"lower" 1499464800000 "upper" 1499468399999} "value" 20}
            {"bounds" {"lower" 1499443200000 "upper" 1499446799999} "value" 20}
            {"bounds" {"lower" 1499461200000 "upper" 1499464799999} "value" 50}
            {"bounds" {"lower" 1499324400000 "upper" 1499327999999} "value" 7}]
           (get-in simulation [:result :aggregates "hourly-page-views"])))))

(deftest test-sum
  (let [service (-> (s/new-service)
                    (t/input-topic "events")
                    (a/aggregate-together
                     [(a/sum "hourly-page-views" "points" (a/fixed-windows-of 1 "hour" "timestamp"))]))
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"bounds" {"lower" 1499464800000 "upper" 1499468399999} "value" 35}
            {"bounds" {"lower" 1499443200000 "upper" 1499446799999} "value" 20}
            {"bounds" {"lower" 1499461200000 "upper" 1499464799999} "value" 65}
            {"bounds" {"lower" 1499324400000 "upper" 1499327999999} "value" 7}]
           (get-in simulation [:result :aggregates "hourly-page-views"])))))

(deftest test-grouped-average
  (let [service (-> (s/new-service)
                    (t/input-topic "events")
                    (a/aggregate-together
                     [(a/average "hourly-page-views" "points" (a/fixed-windows-of 1 "hour" "timestamp"))]
                     ["nation" "team"]))
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= {"usa"
            {"red"
             [{"bounds" {"lower" 1499464800000 "upper" 1499468399999} "value" 20}
              {"bounds" {"lower" 1499461200000 "upper" 1499464799999} "value" 15}]
             "blue"
             [{"bounds" {"lower" 1499464800000 "upper" 1499468399999} "value" 5}]}

            "canada"
            {"red"
             [{"bounds" {"lower" 1499464800000 "upper" 1499468399999} "value" 10}]
             "orange"
             [{"bounds" {"lower" 1499461200000 "upper" 1499464799999} "value" 50}
              {"bounds" {"lower" 1499324400000 "upper" 1499327999999} "value" 7}]}

            "mexico"
            {"green"
             [{"bounds" {"lower" 1499443200000 "upper" 1499446799999} "value" 20}]}}
           (get-in simulation [:result :aggregates "hourly-page-views"])))))

(deftest test-sum-sliding-windows
  (let [service (-> (s/new-service)
                    (t/input-topic "events")
                    (a/aggregate-together
                     [(a/sum "hourly-page-views" "points" (a/sliding-windows-of 1 "hour" 30 "minutes" "timestamp"))]))
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"bounds" {"lower" 1499463000000 "upper" 1499466599999} "value" 85}
            {"bounds" {"lower" 1499464800000 "upper" 1499468399999} "value" 35}
            {"bounds" {"lower" 1499443200000 "upper" 1499446799999} "value" 20}
            {"bounds" {"lower" 1499445000000 "upper" 1499448599999} "value" 20}
            {"bounds" {"lower" 1499459400000 "upper" 1499462999999} "value" 15}
            {"bounds" {"lower" 1499461200000 "upper" 1499464799999} "value" 65}
            {"bounds" {"lower" 1499322600000 "upper" 1499326199999} "value" 7}
            {"bounds" {"lower" 1499324400000 "upper" 1499327999999} "value" 7}]
           (get-in simulation [:result :aggregates "hourly-page-views"])))))

(deftest test-max-global-windows
  (let [service (-> (s/new-service)
                    (t/input-topic "events")
                    (a/aggregate-together
                     [(a/max "hourly-page-views" "points" (a/globally-windowed))]))
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= {"value" 50}
           (get-in simulation [:result :aggregates "hourly-page-views"])))))

(deftest test-count-session-windows
  (let [service (-> (s/new-service)
                    (t/input-topic "events")
                    (a/aggregate-together
                     [(a/count "hourly-page-views" (a/session-windows-of "nation" 1 "day" "timestamp"))]))
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"session-key" "mexico" "bounds" {"lower" 1499445513000 "upper" 1499445513000} "value" 1}
            {"session-key" "usa" "bounds" {"lower" 1499462511000 "upper" 1499465513000} "value" 3}
            {"session-key" "canada" "bounds" {"lower" 1499325313000 "upper" 1499325313000} "value" 1}
            {"session-key" "canada" "bounds" {"lower" 1499463213000 "upper" 1499465513040} "value" 2}]
           (get-in simulation [:result :aggregates "hourly-page-views"])))))
