(ns pyroclast-clojure.v1.roaming.filters-test
  (:require [clojure.test :refer :all]
            [pyroclast-clojure.v1.roaming.client :as roaming]
            [pyroclast-clojure.v1.roaming.filters :as f]
            [pyroclast-clojure.v1.roaming.service :as s]
            [pyroclast-clojure.v1.roaming.topic :as t]
            [pyroclast-clojure.util :as u]))

(deftest ^:roaming test-coll?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/coll? "target")
                    (t/output-topic "output"))
        records [{"target" [1 2 3]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-distinct?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/distinct? "target")
                    (t/output-topic "output"))
        records [{"target" ["a" "b" "c"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-empty?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/empty? "target")
                    (t/output-topic "output"))
        records [{"target" []}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-even?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/even? "target")
                    (t/output-topic "output"))
        records [{"target" 6}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-odd?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/odd? "target")
                    (t/output-topic "output"))
        records [{"target" 3}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-=
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/= "target" 50)
                    (t/output-topic "output"))
        records [{"target" 50}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-not=
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/not= "target" 50)
                    (t/output-topic "output"))
        records [{"target" -39}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-<
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/< "target" 50)
                    (t/output-topic "output"))
        records [{"target" 45}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-<=
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/<= "target" 50)
                    (t/output-topic "output"))
        records [{"target" 50}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test->
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/> "target" 45)
                    (t/output-topic "output"))
        records [{"target" 50}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test->=
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/>= "target" 50)
                    (t/output-topic "output"))
        records [{"target" 50}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-pos?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/pos? "target")
                    (t/output-topic "output"))
        records [{"target" 3}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-neg?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/neg? "target")
                    (t/output-topic "output"))
        records [{"target" -2814}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-integer?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/integer? "target")
                    (t/output-topic "output"))
        records [{"target" -370424}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-map?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/map? "target")
                    (t/output-topic "output"))
        records [{"target" {"a" "b"}}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-nil?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/nil? "target")
                    (t/output-topic "output"))
        records [{}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-number?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/number? "target")
                    (t/output-topic "output"))
        records [{"target" 5.5}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-sequential?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/sequential? "target")
                    (t/output-topic "output"))
        records [{"target" []}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-true?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/true? "target")
                    (t/output-topic "output"))
        records [{"target" true}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-false?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/false? "target")
                    (t/output-topic "output"))
        records [{"target" false}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-zero?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/zero? "target")
                    (t/output-topic "output"))
        records [{"target" 0}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-in?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/in? "target" 2)
                    (t/output-topic "output"))
        records [{"target" [1 2 3]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-string-blank?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/string-blank? "target")
                    (t/output-topic "output"))
        records [{"target" ""}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-string-starts-with?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/string-starts-with? "target" "pri")
                    (t/output-topic "output"))
        records [{"target" "prince"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-string-ends-with?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/string-ends-with? "target" "est")
                    (t/output-topic "output"))
        records [{"target" "greatest"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-string-includes?
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (f/string-includes? "target" "ea")
                    (t/output-topic "output"))
        records [{"target" "feast"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= records (get-in simulation [:result :output-records])))))
