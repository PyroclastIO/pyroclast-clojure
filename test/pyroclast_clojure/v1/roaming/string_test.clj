(ns pyroclast-clojure.roaming.string-test
  (:require [clojure.test :refer :all]
            [pyroclast-clojure.roaming.client :as roaming]
            [pyroclast-clojure.roaming.string :as string]
            [pyroclast-clojure.roaming.service :as s]
            [pyroclast-clojure.roaming.topic :as t]))

(def config {:endpoint "http://localhost:10557"})

(deftest test-capitalize
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/capitalize "first-name")
                    (t/output-topic "output"))
        records [{"first-name" "fred"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"first-name" "Fred"}]
           (get-in simulation [:result :output-records])))))

(deftest test-trim
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/trim "phrase" {:dst "trimmed"})
                    (t/output-topic "output"))
        records [{"phrase" "  Hello world with spaces on the sides.  "}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"phrase" "  Hello world with spaces on the sides.  "
             "trimmed" "Hello world with spaces on the sides."}]
           (get-in simulation [:result :output-records])))))

(deftest test-triml
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/triml "phrase" {:dst "trimmed"})
                    (t/output-topic "output"))
        records [{"phrase" "  Hello world with spaces on the sides.  "}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"phrase" "  Hello world with spaces on the sides.  "
                "trimmed" "Hello world with spaces on the sides.  "}]
           (get-in simulation [:result :output-records])))))

(deftest test-trimr
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/trimr "phrase" {:dst "trimmed"})
                    (t/output-topic "output"))
        records [{"phrase" "  Hello world with spaces on the sides.  "}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"phrase" "  Hello world with spaces on the sides.  "
             "trimmed" "  Hello world with spaces on the sides."}]
           (get-in simulation [:result :output-records])))))

(deftest test-trim-newline
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/trim-newline "phrase" {:dst "trimmed"})
                    (t/output-topic "output"))
        records [{"phrase" "Hello world with a newline on the end.\n"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"phrase" "Hello world with a newline on the end.\n"
             "trimmed" "Hello world with a newline on the end."}]
           (get-in simulation [:result :output-records])))))

(deftest test-string-reverse
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/string-reverse "sport" {:dst "reversed"})
                    (t/output-topic "output"))
        records [{"sport" "soccer"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"sport" "soccer" "reversed" "reccos"}]
           (get-in simulation [:result :output-records])))))

(deftest test-upper-case
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/upper-case "gene-sequence" {:dst "formatted-sequence"})
                    (t/output-topic "output"))
        records [{"gene-sequence" "aatccgctag"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"gene-sequence" "aatccgctag"
             "formatted-sequence" "AATCCGCTAG"}]
           (get-in simulation [:result :output-records])))))

(deftest test-lower-case
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/lower-case "language")
                    (t/output-topic "output"))
        records [{"language" "JAVA"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"language" "java"}]
           (get-in simulation [:result :output-records])))))

(deftest test-split-lines
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/split-lines "haiku" {:dst "lines"})
                    (t/output-topic "output"))
        records [{"haiku" "Blowing from the west\nFallen leaves gather\nIn the east.\n- Yosa Buson"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"haiku" "Blowing from the west\nFallen leaves gather\nIn the east.\n- Yosa Buson"
             "lines" ["Blowing from the west"
                      "Fallen leaves gather"
                      "In the east."
                      "- Yosa Buson"]}]
           (get-in simulation [:result :output-records])))))

(deftest test-split-whitespace
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/split-whitespace "sentence" {:dst "words"})
                    (t/output-topic "output"))
        records [{"sentence" "The rain in spain falls mainly on the plain"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"sentence" "The rain in spain falls mainly on the plain"
                "words" ["The" "rain" "in" "spain" "falls" "mainly" "on" "the" "plain"]}]
           (get-in simulation [:result :output-records])))))

(deftest test-replace
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/replace "sentence" "(\\d+)" "42" {:dst "result"})
                    (t/output-topic "output"))
        records [{"sentence" "I am 30 years old."}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"sentence" "I am 30 years old."
             "result" "I am 42 years old."}]
           (get-in simulation [:result :output-records])))))

(deftest test-replace-first
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/replace-first "sentence" "(\\d+)" "42" {:dst "result"})
                    (t/output-topic "output"))
        records [{"sentence" "I am 30 years old and have 3 cats."}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"sentence" "I am 30 years old and have 3 cats."
             "result" "I am 42 years old and have 3 cats."}]
           (get-in simulation [:result :output-records])))))

(deftest test-concat-keys
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/concat-keys [["city"] ["state"] ["country"]] ", " "location")
                    (t/output-topic "output"))
        records [{"city" "Philadelphia" "state" "Pennsylvania" "country" "USA"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"city" "Philadelphia" "state" "Pennsylvania" "country" "USA"
             "location" "Philadelphia, Pennsylvania, USA"}]
           (get-in simulation [:result :output-records])))))

(deftest test-extract-regex-match
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/extract-regex-match "sentence" "\\d+" {:dst "points"})
                    (t/output-topic "output"))
        records [{"sentence" "The home team scored 13 points, the away 3."}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"sentence" "The home team scored 13 points, the away 3."
             "points" "13"}]
           (get-in simulation [:result :output-records])))))

(deftest test-extract-regex-matches
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/extract-regex-matches "sentence" "\\d+" {:dst "points"})
                    (t/output-topic "output"))
        records [{"sentence" "The home team scored 13 points, the away 3."}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"sentence" "The home team scored 13 points, the away 3."
             "points" ["13" "3"]}]
           (get-in simulation [:result :output-records])))))

(deftest test-format-string
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/format-string [["from"] ["to"]] "%s <-> %s" "traveling")
                    (t/output-topic "output"))
        records [{"from" "Philadelphia" "to" "Seattle"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"from" "Philadelphia"
             "to" "Seattle"
             "traveling" "Philadelphia <-> Seattle"}]
           (get-in simulation [:result :output-records])))))

(deftest test-join
  (let [service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/join "name" " " {:dst "full"})
                    (t/output-topic "output"))
        records [{"name" ["Anna" "Marie" "Walters"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"name" ["Anna" "Marie" "Walters"]
             "full" "Anna Marie Walters"}]
           (get-in simulation [:result :output-records])))))
