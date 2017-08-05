(ns pyroclast-clojure.v1.roaming.string-test
  (:require [clojure.test :refer :all]
            [pyroclast-clojure.v1.roaming.client :as roaming]
            [pyroclast-clojure.v1.roaming.string :as string]
            [pyroclast-clojure.v1.roaming.service :as s]
            [pyroclast-clojure.v1.roaming.topic :as t]
            [pyroclast-clojure.util :as u]))

(deftest ^:roaming test-capitalize
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/capitalize "first-name")
                    (t/output-topic "output"))
        records [{"first-name" "fred"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"first-name" "Fred"}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-trim
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/trim "phrase" {:dst "trimmed"})
                    (t/output-topic "output"))
        records [{"phrase" "  Hello world with spaces on the sides.  "}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"phrase" "  Hello world with spaces on the sides.  "
             "trimmed" "Hello world with spaces on the sides."}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-triml
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/triml "phrase" {:dst "trimmed"})
                    (t/output-topic "output"))
        records [{"phrase" "  Hello world with spaces on the sides.  "}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"phrase" "  Hello world with spaces on the sides.  "
                "trimmed" "Hello world with spaces on the sides.  "}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-trimr
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/trimr "phrase" {:dst "trimmed"})
                    (t/output-topic "output"))
        records [{"phrase" "  Hello world with spaces on the sides.  "}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"phrase" "  Hello world with spaces on the sides.  "
             "trimmed" "  Hello world with spaces on the sides."}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-trim-newline
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/trim-newline "phrase" {:dst "trimmed"})
                    (t/output-topic "output"))
        records [{"phrase" "Hello world with a newline on the end.\n"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"phrase" "Hello world with a newline on the end.\n"
             "trimmed" "Hello world with a newline on the end."}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-string-reverse
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/string-reverse "sport" {:dst "reversed"})
                    (t/output-topic "output"))
        records [{"sport" "soccer"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"sport" "soccer" "reversed" "reccos"}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-upper-case
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/upper-case "gene-sequence" {:dst "formatted-sequence"})
                    (t/output-topic "output"))
        records [{"gene-sequence" "aatccgctag"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"gene-sequence" "aatccgctag"
             "formatted-sequence" "AATCCGCTAG"}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-lower-case
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/lower-case "language")
                    (t/output-topic "output"))
        records [{"language" "JAVA"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"language" "java"}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-split-lines
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
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

(deftest ^:roaming test-split-whitespace
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/split-whitespace "sentence" {:dst "words"})
                    (t/output-topic "output"))
        records [{"sentence" "The rain in spain falls mainly on the plain"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"sentence" "The rain in spain falls mainly on the plain"
                "words" ["The" "rain" "in" "spain" "falls" "mainly" "on" "the" "plain"]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-replace
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/replace "sentence" "(\\d+)" "42" {:dst "result"})
                    (t/output-topic "output"))
        records [{"sentence" "I am 30 years old."}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"sentence" "I am 30 years old."
             "result" "I am 42 years old."}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-replace-first
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/replace-first "sentence" "(\\d+)" "42" {:dst "result"})
                    (t/output-topic "output"))
        records [{"sentence" "I am 30 years old and have 3 cats."}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"sentence" "I am 30 years old and have 3 cats."
             "result" "I am 42 years old and have 3 cats."}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-concat-keys
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/concat-keys [["city"] ["state"] ["country"]] ", " "location")
                    (t/output-topic "output"))
        records [{"city" "Philadelphia" "state" "Pennsylvania" "country" "USA"}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"city" "Philadelphia" "state" "Pennsylvania" "country" "USA"
             "location" "Philadelphia, Pennsylvania, USA"}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-extract-regex-match
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/extract-regex-match "sentence" "\\d+" {:dst "points"})
                    (t/output-topic "output"))
        records [{"sentence" "The home team scored 13 points, the away 3."}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"sentence" "The home team scored 13 points, the away 3."
             "points" "13"}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-extract-regex-matches
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/extract-regex-matches "sentence" "\\d+" {:dst "points"})
                    (t/output-topic "output"))
        records [{"sentence" "The home team scored 13 points, the away 3."}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"sentence" "The home team scored 13 points, the away 3."
             "points" ["13" "3"]}]
           (get-in simulation [:result :output-records])))))

(deftest ^:roaming test-format-string
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
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

(deftest ^:roaming test-join
  (let [config (:roaming (u/load-config "config.edn"))
        service (-> (s/new-service)
                    (t/input-topic "input")
                    (string/join "name" " " {:dst "full"})
                    (t/output-topic "output"))
        records [{"name" ["Anna" "Marie" "Walters"]}]
        simulation (roaming/simulate! config service records)]
    (is (:success? simulation))
    (is (= [{"name" ["Anna" "Marie" "Walters"]
             "full" "Anna Marie Walters"}]
           (get-in simulation [:result :output-records])))))
