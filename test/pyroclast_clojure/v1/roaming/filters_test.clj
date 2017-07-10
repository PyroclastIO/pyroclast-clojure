(ns pyroclast-clojure.roaming.filters-test
  (:require [clojure.test :refer :all]
            [pyroclast-clojure.roaming.client :as roaming]
            [pyroclast-clojure.roaming.filters :as f]
            [pyroclast-clojure.roaming.service :as s]
            [pyroclast-clojure.roaming.topic :as t]))

(def config {:endpoint "http://localhost:10557"})

(deftest test-coll?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/coll? "target")
                    (t/output-topic "output"))
        records [{"target" [1 2 3]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-distinct?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/distinct? "target")
                    (t/output-topic "output"))
        records [{"target" ["a" "b" "c"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-empty?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/empty? "target")
                    (t/output-topic "output"))
        records [{"target" []}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-even?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/even? "target")
                    (t/output-topic "output"))
        records [{"target" 6}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-odd?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/odd? "target")
                    (t/output-topic "output"))
        records [{"target" 3}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-=
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/= "target" 50)
                    (t/output-topic "output"))
        records [{"target" 50}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-not=
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/not= "target" 50)
                    (t/output-topic "output"))
        records [{"target" -39}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-<
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/< "target" 50)
                    (t/output-topic "output"))
        records [{"target" 45}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-<=
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/<= "target" 50)
                    (t/output-topic "output"))
        records [{"target" 50}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test->
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/> "target" 45)
                    (t/output-topic "output"))
        records [{"target" 50}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test->=
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/>= "target" 50)
                    (t/output-topic "output"))
        records [{"target" 50}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-pos?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/pos? "target")
                    (t/output-topic "output"))
        records [{"target" 3}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-neg?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/neg? "target")
                    (t/output-topic "output"))
        records [{"target" -2814}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-integer?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/integer? "target")
                    (t/output-topic "output"))
        records [{"target" -370424}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-map?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/map? "target")
                    (t/output-topic "output"))
        records [{"target" {"a" "b"}}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-nil?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/nil? "target")
                    (t/output-topic "output"))
        records [{}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-number?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/number? "target")
                    (t/output-topic "output"))
        records [{"target" 5.5}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-sequential?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/sequential? "target")
                    (t/output-topic "output"))
        records [{"target" []}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-true?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/true? "target")
                    (t/output-topic "output"))
        records [{"target" true}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-false?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/false? "target")
                    (t/output-topic "output"))
        records [{"target" false}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-zero?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/zero? "target")
                    (t/output-topic "output"))
        records [{"target" 0}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-in?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/in? "target" 2)
                    (t/output-topic "output"))
        records [{"target" [1 2 3]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-string-blank?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/string-blank? "target")
                    (t/output-topic "output"))
        records [{"target" ""}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-string-starts-with?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/string-starts-with? "target" "pri")
                    (t/output-topic "output"))
        records [{"target" "prince"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-string-ends-with?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/string-ends-with? "target" "est")
                    (t/output-topic "output"))
        records [{"target" "greatest"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest test-string-includes?
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/string-includes? "target" "ea")
                    (t/output-topic "output"))
        records [{"target" "feast"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))
