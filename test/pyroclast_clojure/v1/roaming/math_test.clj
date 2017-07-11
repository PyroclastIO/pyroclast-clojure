(ns pyroclast-clojure.v1.roaming.math-test
  (:require [clojure.test :refer :all]
            [pyroclast-clojure.v1.roaming.client :as roaming]
            [pyroclast-clojure.v1.roaming.math :as math]
            [pyroclast-clojure.v1.roaming.service :as s]
            [pyroclast-clojure.v1.roaming.topic :as t]
            [pyroclast-clojure.util :as u]))

(deftest ^:roaming test-plus
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/plus "n" 4 {:dst "result"})
                    (t/output-topic "output"))
        records [{"n" 7}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 7 "result" 11}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-dynamic-plus
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/dynamic-plus "n" "m" {:dst "result"})
                    (t/output-topic "output"))
        records [{"n" 7 "m" 3}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"n" 7 "m" 3 "result" 10}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-minus
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/minus "n" 8 {:dst "result"})
                    (t/output-topic "output"))
        records [{"n" 43}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 43 "result" 35}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-dynamic-minus
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/dynamic-minus "n" "m" {:dst "result"})
                    (t/output-topic "output"))
        records [{"n" 43 "m" 8}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 43 "m" 8 "result" 35}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-times
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/times "n" 7 {:dst "result"})
                    (t/output-topic "output"))
        records [{"n" 8}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 8 "result" 56}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-dynamic-times
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/dynamic-times "n" "m" {:dst "result"})
                    (t/output-topic "output"))
        records [{"n" 8 "m" 5}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 8 "m" 5 "result" 40}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-divide
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/divide "n" 16 {:dst ["result"]})
                    (t/output-topic "output"))
        records [{"n" 64}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 64 "result" 4}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-dynamic-divide
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/dynamic-divide "n" "m" {:dst "result"})
                    (t/output-topic "output"))
        records [{"n" 12 "m" 4}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"n" 12 "m" 4 "result" 3}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-mod
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/mod "n" 5 {:dst "result"})
                    (t/output-topic "output"))
        records [{"n" 42}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 42 "result" 2}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-dynamic-mod
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/dynamic-mod "n" "m" {:dst "result"})
                    (t/output-topic "output"))
        records [{"n" 42 "m" 5}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"n" 42 "m" 5 "result" 2}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-quotient
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/quotient "n" 5 {:dst "result"})
                    (t/output-topic "output"))
        records [{"n" 24}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 24 "result" 4}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-dynamic-quotient
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/dynamic-quotient "n" "m" {:dst "result"})
                    (t/output-topic "output"))
        records [{"n" 24 "m" 5}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 24 "m" 5 "result" 4}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-remainder
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/remainder "n" 5 {:dst "result"})
                    (t/output-topic "output"))
        records [{"n" 24}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 24 "result" 4}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-dynamic-remainder
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/dynamic-remainder "n" "m" {:dst "result"})
                    (t/output-topic "output"))
        records [{"n" 24 "m" 5}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 24 "m" 5 "result" 4}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-pow
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/pow "n" 4 {:dst "to-pow"})
                    (t/output-topic "output"))
        records [{"n" 3}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 3 "to-pow" 81.0}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-dynamic-pow
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/dynamic-pow "n" "m" {:dst "result"})
                    (t/output-topic "output"))
        records [{"n" 3 "m" 4}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 3 "m" 4 "result" 81.0}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-abs
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/abs "number" {:dst "absolute-value"})
                    (t/output-topic "output"))
        records [{"number" -5.3}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"number" -5.3 "absolute-value" 5.3}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-cos
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/cos "angle" {:dst "cos-angle"})
                    (t/output-topic "output"))
        records [{"angle" 0.3}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"angle" 0.3 "cos-angle" 0.955336489125606}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-acos
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/acos "angle" {:dst "acos-angle"})
                    (t/output-topic "output"))
        records [{"angle" 0.3}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"angle" 0.3 "acos-angle" 1.2661036727794992}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-cosh
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/cosh "angle" {:dst "cosh-angle"})
                    (t/output-topic "output"))
        records [{"angle" 0.3}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"angle" 0.3 "cosh-angle" 1.0453385141288605}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-sin
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/sin "angle" {:dst "sin-angle"})
                    (t/output-topic "output"))
        records [{"angle" 0.45}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"angle" 0.45 "sin-angle" 0.43496553411123023}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-asin
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/asin "angle" {:dst "asin-angle"})
                    (t/output-topic "output"))
        records [{"angle" 0.45}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"angle" 0.45 "asin-angle" 0.4667653390472964}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-sinh
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/sinh "angle" {:dst "sinh-angle"})
                    (t/output-topic "output"))
        records [{"angle" 0.45}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"angle" 0.45 "sinh-angle" 0.46534201693419774}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-tan
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/tan "angle" {:dst "tan-angle"})
                    (t/output-topic "output"))
        records [{"angle" 0.82}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"angle" 0.82 "tan-angle" 1.0717137226410736}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-atan
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/atan "angle" {:dst "atan-angle"})
                    (t/output-topic "output"))
        records [{"angle" 0.82}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"angle" 0.82 "atan-angle" 0.6868176497586452}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-tanh
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/tanh "angle" {:dst "tanh-angle"})
                    (t/output-topic "output"))
        records [{"angle" 0.82}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"angle" 0.82 "tanh-angle" 0.6750698748386078}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-sqrt
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/sqrt "n" {:dst "square-root"})
                    (t/output-topic "output"))
        records [{"n" 36}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 36 "square-root" 6.0}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-cbrt
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/cbrt "n" {:dst "cubed-root"})
                    (t/output-topic "output"))
        records [{"n" 27}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 27 "cubed-root" 3.0}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-exp
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/exp "n" {:dst "euler"})
                    (t/output-topic "output"))
        records [{"n" -1}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" -1 "euler" 0.36787944117144233}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-expm1
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/expm1 "n" {:dst "euler"})
                    (t/output-topic "output"))
        records [{"n" -1}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" -1 "euler" -0.6321205588285577}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-floor
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/floor "n" {:dst "floor"})
                    (t/output-topic "output"))
        records [{"n" 5.3}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 5.3 "floor" 5.0}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-ceil
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/ceil "n" {:dst "rounded-up"})
                    (t/output-topic "output"))
        records [{"n" 10.9}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 10.9 "rounded-up" 11.0}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-log
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/log "n")
                    (t/output-topic "output"))
        records [{"n" 88}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 4.477336814478207}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-log10
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/log10 "n")
                    (t/output-topic "output"))
        records [{"n" 88}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 1.9444826721501687}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-log1p
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/log1p "n")
                    (t/output-topic "output"))
        records [{"n" 88}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 4.48863636973214}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-round
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/round "n" {:dst "rounded"})
                    (t/output-topic "output"))
        records [{"n" 7.51}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"n" 7.51 "rounded" 8}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-round-decimals
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/round-decimals "n" 2 {:dst "rounded"})
                    (t/output-topic "output"))
        records [{"n" 7.5153}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"n" 7.5153 "rounded" 7.52}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-to-degrees
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/to-degrees "radians" {:dst "degrees"})
                    (t/output-topic "output"))
        records [{"radians" 45}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"radians" 45 "degrees" 2578.3100780887044}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-to-radians
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/to-radians "degrees" {:dst "radians"})
                    (t/output-topic "output"))
        records [{"degrees" 45}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"degrees" 45 "radians" 0.7853981633974483}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-min
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/min "numbers" {:dst "minimum"})
                    (t/output-topic "output"))
        records [{"numbers" [86 34 7 109]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"numbers" [86 34 7 109] "minimum" 7}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-max
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/max "numbers" {:dst "maximum"})
                    (t/output-topic "output"))
        records [{"numbers" [86 34 7 109]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"numbers" [86 34 7 109] "maximum" 109}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-atan2
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/atan2 3 4 "result")
                    (t/output-topic "output"))
        records [{"score" 36}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"score" 36 "result" 0.6435011087932844}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-atan2-dynamic
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/atan2-dynamic "x" "y" "result")
                    (t/output-topic "output"))
        records [{"x" 3 "y" 4}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation) simulation)
    (is (= [{"x" 3 "y" 4 "result" 0.6435011087932844}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-hypot
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/hypot 3 4 "result")
                    (t/output-topic "output"))
        records [{"score" 26}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"score" 26 "result" 5.0}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-hypot-dynamic
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (math/hypot-dynamic "x" "y" "result")
                    (t/output-topic "output"))
        records [{"x" 3 "y" 4}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"x" 3 "y" 4 "result" 5.0}]
           (get-in simulation [:result :output-records])))))
