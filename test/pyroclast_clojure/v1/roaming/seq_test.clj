(ns pyroclast-clojure.v1.roaming.seq-test
  (:require [clojure.test :refer :all]
            [pyroclast-clojure.v1.roaming.client :as roaming]
            [pyroclast-clojure.v1.roaming.seq :as pseq]
            [pyroclast-clojure.v1.roaming.service :as s]
            [pyroclast-clojure.v1.roaming.topic :as t]
            [pyroclast-clojure.util :as u]))

(deftest ^:roaming test-first
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/first "animals" {:dst "first-animal"})
                    (t/output-topic "output"))
        records [{:animals ["cat" "dog" "alligator"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"animals" ["cat" "dog" "alligator"]
             "first-animal" "cat"}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-second
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/second "animals" {:dst "second-animal"})
                    (t/output-topic "output"))
        records [{"animals" ["cat" "dog" "alligator"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"animals" ["cat" "dog" "alligator"]
             "second-animal" "dog"}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-rest
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/rest "animals" {:dst "rest-of-animals"})
                    (t/output-topic "output"))
        records [{"animals" ["cat" "dog" "alligator"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"animals" ["cat" "dog" "alligator"]
             "rest-of-animals" ["dog" "alligator"]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-last
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/last "animals" {:dst "last-animal"})
                    (t/output-topic "output"))
        records [{"animals" ["cat" "dog" "alligator"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"animals" ["cat" "dog" "alligator"]
             "last-animal" "alligator"}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-butlast
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/butlast "animals" {:dst "all-but-last"})
                    (t/output-topic "output"))
        records [{"animals" ["cat" "dog" "alligator"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"animals" ["cat" "dog" "alligator"]
             "all-but-last" ["cat" "dog"]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-count
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/count "animals" {:dst "n"})
                    (t/output-topic "output"))
        records [{"animals" ["cat" "dog" "alligator"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"animals" ["cat" "dog" "alligator"]
             "n" 3}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-reverse
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/reverse "numbers")
                    (t/output-topic "output"))
        records [{"numbers" [1 2 3 4 5 6]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"numbers" [6 5 4 3 2 1]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-sort
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/sort "numbers" {:dst "sorted"})
                    (t/output-topic "output"))
        records [{"numbers" [6 3 7 8 3 7 0 9]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"numbers" [6 3 7 8 3 7 0 9]
             "sorted" [0 3 3 6 7 7 8 9]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-shuffle
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/shuffle "numbers" {:dst "shuffled"})
                    (t/output-topic "output"))
        records [{"numbers" [6 3 7 8 3 7 0 9]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (not (empty? (get-in simulation [:result :output-records]))))))

(deftest ^:roaming test-frequencies
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/frequencies "sentence" {:dst "frequencies"})
                    (t/output-topic "output"))
        records [{"sentence" "Hula hooping hooligans."}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"sentence" "Hula hooping hooligans."
             "frequencies" {" " 2 "a" 2 "g" 2 "H" 1 "h" 2 "i" 2 "l" 2 "." 1 "n" 2 "o" 4 "p" 1 "s" 1 "u" 1}}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-explode
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/explode "tags" {:dst "tag"})
                    (t/output-topic "output"))
        records [{"tags" ["retail" "shoes" "discount"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"tag" "retail"}
            {"tag" "shoes"}
            {"tag" "discount"}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-keys
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/keys "date" {:dst "keys"})
                    (t/output-topic "output"))
        records [{"date" {"year" 2017 "month" "June" "day" 1}}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"date" {"year" 2017 "month" "June" "day" 1}
             "keys" ["year" "month" "day"]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-vals
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/vals "date")
                    (t/output-topic "output"))
        records [{"date" {"year" 2017 "month" "June" "day" 1}}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"date" [2017 "June" 1]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-seq
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/seq "profile" {:dst "pairs"})
                    (t/output-topic "output"))
        records [{"profile" {"color" "blue" "bicycle" "green"}}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"profile" {"color" "blue" "bicycle" "green"}
             "pairs" [["color" "blue"] ["bicycle" "green"]]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-distinct
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/distinct "letters")
                    (t/output-topic "output"))
        records [{"letters" ["a" "a" "b" "c" "d" "c" "a"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"letters" ["a" "b" "c" "d"]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-take
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/take "animals" 2 {:dst "result"})
                    (t/output-topic "output"))
        records [{"animals" ["cat" "dog" "alligator"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"animals" ["cat" "dog" "alligator"]
             "result" ["cat" "dog"]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-take-nth
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/take-nth "animals" 2 {:dst "result"})
                    (t/output-topic "output"))
        records [{"animals" ["cat" "dog" "alligator" "llama" "sheep"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"animals" ["cat" "dog" "alligator" "llama" "sheep"]
             "result" ["cat" "alligator" "sheep"]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-drop
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/drop "animals" 2 {:dst "result"})
                    (t/output-topic "output"))
        records [{"animals" ["cat" "dog" "alligator"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"animals" ["cat" "dog" "alligator"]
             "result" ["alligator"]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-drop-last
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/drop-last "animals" 2 {:dst "result"})
                    (t/output-topic "output"))
        records [{"animals" ["cat" "dog" "alligator" "llama" "sheep"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"animals" ["cat" "dog" "alligator" "llama" "sheep"]
             "result" ["cat" "dog" "alligator"]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-nth
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/nth "animals" 2 {:dst "third-animal"})
                    (t/output-topic "output"))
        records [{"animals" ["cat" "dog" "alligator" "sheep"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"animals" ["cat" "dog" "alligator" "sheep"]
             "third-animal" "alligator"}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-rand-nth
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/rand-nth "animals" {:dst "random-animal"})
                    (t/output-topic "output"))
        records [{"animals" ["cat" "dog" "alligator" "sheep"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (not (empty? (get-in simulation [:result :output-records]))))))

(deftest ^:roaming test-move
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/move ["person" "name" 0] ["first-name"])
                    (t/output-topic "output"))
        records [{"person" {"name" ["Martin" "Luther" "King"]}}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"person" {"name" ["Martin" "Luther" "King"]}
             "first-name" "Martin"}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-assoc-in
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/assoc-in ["person" "name" "title"] "Mr.")
                    (t/output-topic "output"))
        records [{"person" {"name" {"first" "Bob" "last" "Richards"}}}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"person" {"name" {"first" "Bob" "last" "Richards" "title" "Mr."}}}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-dynamic-assoc-in
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/dynamic-assoc-in ["profile" "first-name"] ["person" "name" "first"])
                    (t/output-topic "output"))
        records [{"person" {"name" {"first" "Bob" "last" "Richards"}}}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"person" {"name" {"first" "Bob" "last" "Richards"}}
             "profile" {"first-name" "Bob"}}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-assoc-under
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/assoc-under "user")
                    (t/output-topic "output"))
        records [{"person" {"name" {"first" "Bob" "last" "Richards"}}}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"user" {"person" {"name" {"first" "Bob" "last" "Richards"}}}}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-dissoc
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/dissoc ["color" "time"])
                    (t/output-topic "output"))
        records [{"color" "red" "day" "Wednesday" "time" "morning"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"day" "Wednesday"}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-zipmap
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/zipmap "name" ["first" "middle" "last"])
                    (t/output-topic "output"))
        records [{"name" ["Mary" "Marie" "Smith"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"name" {"first" "Mary" "middle" "Marie" "last" "Smith"}}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-zipmap-dynamic
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/dynamic-zipmap "name" "parts" {:dst "zipped"})
                    (t/output-topic "output"))
        records [{"name" ["Mary" "Marie" "Smith"]
                  "parts" ["first" "middle" "last"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"name" ["Mary" "Marie" "Smith"]
             "parts" ["first" "middle" "last"]
             "zipped" {"first" "Mary" "middle" "Marie" "last" "Smith"}}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-repeat
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/repeat "letter" 3 {:dst "letters"})
                    (t/output-topic "output"))
        records [{"letter" "a"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"letter" "a"
             "letters" ["a" "a" "a"]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-dynamic-repeat
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/dynamic-repeat "letter" "n" {:dst "letters"})
                    (t/output-topic "output"))
        records [{"letter" "a" "n" 4}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"letter" "a"
             "n" 4
             "letters" ["a" "a" "a" "a"]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-select-keys
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/select-keys ["name" "team"])
                    (t/output-topic "output"))
        records [{"name" "fred" "age" 35 "team" "falcons"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"name" "fred" "team" "falcons"}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-merge
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/merge "profile" {"team" "falcons"} {:dst "result"})
                    (t/output-topic "output"))
        records [{"profile" {"name" "fred" "age" 35}}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"profile" {"name" "fred" "age" 35}
             "result" {"name" "fred" "age" 35 "team" "falcons"}}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-dynamic-merge
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/dynamic-merge "profile" "map" {:dst "result"})
                    (t/output-topic "output"))
        records [{"profile" {"name" "fred" "age" 35}
                  "map" {"team" "falcons"}}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"profile" {"name" "fred" "age" 35}
             "map" {"team" "falcons"}
             "result" {"name" "fred" "age" 35 "team" "falcons"}}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-partition
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/partition "team" 2 {:dst "subteams"})
                    (t/output-topic "output"))
        records [{"team" ["stu" "mindy" "ron" "bill" "alison"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"team" ["stu" "mindy" "ron" "bill" "alison"]
             "subteams" [["stu" "mindy"] ["ron" "bill"]]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-dynamic-partition
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/dynamic-partition "team" "n" {:dst "subteams"})
                    (t/output-topic "output"))
        records [{"team" ["stu" "mindy" "ron" "bill" "alison"]
                  "n" 2}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"team" ["stu" "mindy" "ron" "bill" "alison"]
             "subteams" [["stu" "mindy"] ["ron" "bill"]]
             "n" 2}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-partition-all
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/partition-all "team" 3 {:dst "subteams"})
                    (t/output-topic "output"))
        records [{"team" ["stu" "mindy" "ron" "bill" "alison"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"team" ["stu" "mindy" "ron" "bill" "alison"]
             "subteams" [["stu" "mindy" "ron"] ["bill" "alison"]]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-dynamic-partition-all
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (pseq/dynamic-partition-all "team" "n" {:dst "subteams"})
                    (t/output-topic "output"))
        records [{"team" ["stu" "mindy" "ron" "bill" "alison"]
                  "n" 3}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"team" ["stu" "mindy" "ron" "bill" "alison"]
             "subteams" [["stu" "mindy" "ron"] ["bill" "alison"]]
             "n" 3}]
           (get-in simulation [:result :output-records])))))
