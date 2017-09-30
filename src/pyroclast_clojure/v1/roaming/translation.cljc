(ns pyroclast-clojure.v1.roaming.translation
  (:require [clojure.set :refer [rename-keys]]
            [pyroclast-clojure.util :as u]))

(defmulti canonicalize
  (fn [task]
    (:operation task)))

(defmethod canonicalize "readFromTopic"
  [{:keys [params] :as task}]
  {:roaming.task/task-bundle-name :input/kafka-reader
   :task/bundle-version 0
   :params {:input.kafka-reader/topic {:topic/id (:topicId params)}
            :input.kafka-reader/wrap-with-metadata? (:wrapWithMetadata params)}})

(defmethod canonicalize "writeToTopic"
  [{:keys [params] :as task}]
  {:roaming.task/task-bundle-name :output/kafka-writer
   :task/bundle-version 0
   :params {:output.kafka-writer/topic {:topic/id (:topicId params)}}})

(defmethod canonicalize "butlast"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.butlast/target-key-path
              :dst :transformation.butlast/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/butlast
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "floor"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.floor/target-key-path
              :dst :transformation.floor/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/floor
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "log1p"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.log1p/target-key-path
              :dst :transformation.log1p/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/log1p
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "zipmap"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.zipmap/target-key-path
              :keys :transformation.zipmap/keys
              :keysPath :transformation.zipmap/keys-key-path
              :dst :transformation.zipmap/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/zipmap
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "ceil"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.ceil/target-key-path
              :dst :transformation.ceil/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/ceil
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "seq"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.seq/target-key-path
              :dst :transformation.seq/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/seq
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isMap"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.map-pred/target-key-path
              :negated :filter.map-pred/negated?
              :throwOnFalse :filter.map-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/map-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "joinByKeyS3"
  [{:keys [params] :as task}]
  (let [kmap {:dst :transformation.join-by-key-s3/result-key-path
              :joinKey :transformation.join-by-key-s3/join-key
              :s3AccessKey :transformation.join-by-key-s3/access-key
              :s3SecretKey :transformation.join-by-key-s3/secret-key
              :s3Bucket :transformation.join-by-key-s3/bucket
              :s3Key :transformation.join-by-key-s3/key}]
    {:roaming.task/task-bundle-name :transformation/join-by-key-s3
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isDistinct"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.distinct-pred/target-key-path
              :negated :filter.distinct-pred/negated?
              :throwOnFalse :filter.distinct-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/distinct-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "cosh"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.cosh/target-key-path
              :dst :transformation.cosh/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/cosh
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isNumber"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.number-pred/target-key-path
              :negated :filter.number-pred/negated?
              :throwOnFalse :filter.number-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/number-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "min"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.min/target-key-path
              :dst :transformation.min/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/min
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "trim"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.trim/target-key-path
              :dst :transformation.trim/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/trim
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isCollection"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.coll-pred/target-key-path
              :negated :filter.coll-pred/negated?
              :throwOnFalse :filter.coll-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/coll-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "splitLines"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.split-lines/target-key-path
              :dst :transformation.split-lines/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/split-lines
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "keys"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.keys/target-key-path
              :dst :transformation.keys/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/keys
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isEven"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.even-pred/target-key-path
              :negated :filter.even-pred/negated?
              :throwOnFalse :filter.even-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/even-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isNegative"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.neg-pred/target-key-path
              :negated :filter.neg-pred/negated?
              :throwOnFalse :filter.neg-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/neg-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isZero"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.zero-pred/target-key-path
              :negated :filter.zero-pred/negated?
              :throwOnFalse :filter.zero-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/zero-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "lowerCase"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.lower-case/target-key-path
              :dst :transformation.lower-case/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/lower-case
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isNull"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.nil-pred/target-key-path
              :negated :filter.nil-pred/negated?
              :throwOnFalse :filter.nil-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/nil-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isPositive"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.pos-pred/target-key-path
              :negated :filter.pos-pred/negated?
              :throwOnFalse :filter.pos-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/pos-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "dissoc"
  [{:keys [params] :as task}]
  (let [kmap {:keys :transformation.dissoc/target-keys}]
    {:roaming.task/task-bundle-name :transformation/dissoc
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "greaterThan"
  [{:keys [params] :as task}]
  (let [kmap {:leftPath :filter.greater-than-pred/left-key-path
              :constant :filter.greater-than-pred/right-constant
              :negated :filter.greater-than-pred/negated?
              :throwOnFalse :filter.greater-than-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/greater-than-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "atan2"
  [{:keys [params] :as task}]
  (let [kmap {:dst :transformation.atan2/result-key-path
              :x :transformation.atan2/x
              :xPath :transformation.atan2/x-key
              :y :transformation.atan2/y
              :yPath :transformation.atan2/y-key}]
    {:roaming.task/task-bundle-name :transformation/atan2
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "splitWhitespace"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.split-whitespace/target-key-path
              :dst :transformation.split-whitespace/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/split-whitespace
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "quotient"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.quotient/target-key-path
              :constant :transformation.quotient/constant
              :operationPath :transformation.quotient/operation-key-path
              :dst :transformation.quotient/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/quotient
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "reverse"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.reverse/target-key-path
              :dst :transformation.reverse/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/reverse
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "hash"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.hash/target-key-path}]
    {:roaming.task/task-bundle-name :transformation/hash
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "cbrt"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.cbrt/target-key-path
              :dst :transformation.cbrt/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/cbrt
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "repeat"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.repeat/target-key-path
              :n :transformation.repeat/n
              :nPath :transformation.repeat/n-key-path
              :dst :transformation.repeat/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/repeat
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "parseValues"
  [{:keys [params] :as task}]
  (let [kmap {:settings :transformation.parse-vals/parse-settings}]
    {:roaming.task/task-bundle-name :transformation/parse-vals
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "replaceFirst"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.replace-first/target-key-path
              :pattern :transformation.replace-first/pattern
              :replacement :transformation.replace-first/replacement
              :dst :transformation.replace-first/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/replace-first
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "second"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.second/target-key-path
              :dst :transformation.second/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/second
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isIn"
  [{:keys [params] :as task}]
  (let [kmap {:collPath :filter.in-pred/coll-key-path
              :value :filter.in-pred/search-value-constant
              :negated :filter.in-pred/negated?
              :throwOnFalse :filter.in-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/in-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "dropLast"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.drop-last/target-key-path
              :index :transformation.drop-last/index
              :indexPath :transformation.drop-last/index-key-path
              :dst :transformation.drop-last/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/drop-last
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "max"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.max/target-key-path
              :dst :transformation.max/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/max
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isSequential"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.sequential-pred/target-key-path
              :negated :filter.sequential-pred/negated?
              :throwOnFalse :filter.sequential-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/sequential-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "nth"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.nth/target-key-path
              :index :transformation.nth/index
              :dst :transformation.nth/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/nth
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "assocUnder"
  [{:keys [params] :as task}]
  (let [kmap {:targetKey :transformation.assoc-under/target-key}]
    {:roaming.task/task-bundle-name :transformation/assoc-under
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "partition"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.partition/target-key-path
              :n :transformation.partition/n
              :nPath :transformation.partition/n-key-path
              :dst :transformation.partition/result-key-path
              :step :transformation.partition/step
              :pad :transformation.partition/pad}]
    {:roaming.task/task-bundle-name :transformation/partition
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "pow"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.pow/target-key-path
              :constant :transformation.pow/constant
              :operationPath :transformation.pow/operation-key-path
              :dst :transformation.pow/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/pow
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "shuffle"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.shuffle/target-key-path
              :dst :transformation.shuffle/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/shuffle
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "take"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.take/target-key-path
              :index :transformation.take/index
              :dst :transformation.take/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/take
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "rest"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.rest/target-key-path
              :dst :transformation.rest/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/rest
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "minus"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.minus/target-key-path
              :constant :transformation.minus/constant
              :operationPath :transformation.minus/operation-key-path
              :dst :transformation.minus/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/minus
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "count"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.count/target-key-path
              :dst :transformation.count/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/count
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "atan"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.atan/target-key-path
              :dst :transformation.atan/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/atan
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "joinByKeyStatic"
  [{:keys [params] :as task}]
  (let [kmap {:joinKey :transformation.join-by-key-static/join-key
              :staticRecords :transformation.join-by-key-static/static-records
              :dst :transformation.join-by-key-static/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/join-by-key-static
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "sort"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.sort/target-key-path
              :dst :transformation.sort/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/sort
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "log"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.log/target-key-path
              :dst :transformation.log/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/log
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "trimNewline"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.trim-newline/target-key-path
              :dst :transformation.trim-newline/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/trim-newline
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "upperCase"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.upper-case/target-key-path
              :dst :transformation.upper-case/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/upper-case
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "frequencies"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.frequencies/target-key-path
              :dst :transformation.frequencies/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/frequencies
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "parseDatetime"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.parse-datetime/target-key-path
              :format :transformation.parse-datetime/format
              :dst :transformation.parse-datetime/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/parse-datetime
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "greaterThanOrEqualTo"
  [{:keys [params] :as task}]
  (let [kmap {:leftPath :filter.greater-than-or-equal-to-pred/left-key-path
              :constant :filter.greater-than-or-equal-to-pred/right-constant
              :negated :filter.greater-than-or-equal-to-pred/negated?
              :throwOnFalse :filter.greater-than-or-equal-to-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/greater-than-or-equal-to-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "times"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.times/target-key-path
              :constant :transformation.times/constant
              :operationPath :transformation.times/operation-key-path
              :dst :transformation.times/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/times
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "distinct"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.distinct/target-key-path
              :dst :transformation.distinct/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/distinct
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "toDegrees"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.to-degrees/target-key-path
              :dst :transformation.to-degrees/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/to-degrees
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "sin"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.sin/target-key-path
              :dst :transformation.sin/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/sin
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "triml"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.triml/target-key-path
              :dst :transformation.triml/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/triml
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "lessThanOrEqualTo"
  [{:keys [params] :as task}]
  (let [kmap {:leftPath :filter.less-than-or-equal-to-pred/left-key-path
              :rightConstant :filter.less-than-or-equal-to-pred/right-constant
              :negated :filter.less-than-or-equal-to-pred/negated?
              :throwOnFalse :filter.less-than-or-equal-to-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/less-than-or-equal-to-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "replace"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.replace/target-key-path
              :pattern :transformation.replace/pattern
              :replacement :transformation.replace/replacement
              :dst :transformation.replace/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/replace
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "stringReverse"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.string-reverse/target-key-path
              :dst :transformation.string-reverse/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/string-reverse
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isOdd"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.odd-pred/target-key-path
              :negated :filter.odd-pred/negated?
              :throwOnFalse :filter.odd-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/odd-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "hypot"
  [{:keys [params] :as task}]
  (let [kmap {:dst :transformation.hypot/result-key-path
              :x :transformation.hypot/x
              :xPath :transformation.hypot/x-key
              :y :transformation.hypot/y
              :yPath :transformation.hypot/y-key}]
    {:roaming.task/task-bundle-name :transformation/hypot
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "plus"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.plus/target-key-path
              :constant :transformation.plus/constant
              :operationPath :transformation.plus/operation-key-path
              :dst :transformation.plus/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/plus
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "drop"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.drop/target-key-path
              :index :transformation.drop/index
              :indexPath :transformation.drop/index-key-path
              :dst :transformation.drop/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/drop
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "vals"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.vals/target-key-path
              :dst :transformation.vals/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/vals
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isTrue"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.true-pred/target-key-path
              :negated :filter.true-pred/negated?
              :throwOnFalse :filter.true-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/true-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "formatString"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.format-string/multi-target-key-path
              :dst :transformation.format-string/result-key-path
              :format :transformation.format-string/format}]
    {:roaming.task/task-bundle-name :transformation/format-string
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "remainder"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.remainder/target-key-path
              :constant :transformation.remainder/constant
              :operationPath :transformation.remainder/operation-key-path
              :dst :transformation.remainder/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/remainder
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "stringBlank"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.string-blank-pred/target-key-path
              :negated :filter.string-blank-pred/negated?
              :throwOnFalse :filter.string-blank-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/string-blank-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "exp"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.exp/target-key-path
              :dst :transformation.exp/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/exp
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "capitalize"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.capitalize/target-key-path
              :dst :transformation.capitalize/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/capitalize
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "extractRegexMatches"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.extract-regex-matches/target-key-path
              :pattern :transformation.extract-regex-matches/pattern
              :dst :transformation.extract-regex-matches/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/extract-regex-matches
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "stringIncludes"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.string-includes-pred/target-key-path
              :substring :filter.string-includes-pred/substr-constant
              :negated :filter.string-includes-pred/negated?
              :throwOnFalse :filter.string-includes-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/string-includes-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "roundDecimals"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.round-decimals/target-key-path
              :n :transformation.round-decimals/n-decimals
              :dst :transformation.round-decimals/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/round-decimals
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "log10"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.log10/target-key-path
              :dst :transformation.log10/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/log10
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "concatKeys"
  [{:keys [params] :as task}]
  (let [kmap {:dst :transformation.concat-keys/result-key-path
              :src :transformation.concat-keys/multi-target-key-path
              :separator :transformation.concat-keys/separator}]
    {:roaming.task/task-bundle-name :transformation/concat-keys
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isInteger"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.integer-pred/target-key-path
              :negated :filter.integer-pred/negated?
              :throwOnFalse :filter.integer-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/integer-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "customFunction"
  [{:keys [params] :as task}]
  (let [kmap {:code :transformation.execute-javascript/javascript}]
    {:roaming.task/task-bundle-name :transformation/execute-javascript
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "formatUnixMsTimestamp"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.format-unix-ms-timestamp/target-key-path
              :format :transformation.format-unix-ms-timestamp/format
              :dst :transformation.format-unix-ms-timestamp/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/format-unix-ms-timestamp
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "randNth"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.rand-nth/target-key-path
              :dst :transformation.rand-nth/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/rand-nth
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "sqrt"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.sqrt/target-key-path
              :dst :transformation.sqrt/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/sqrt
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "selectKeys"
  [{:keys [params] :as task}]
  (let [kmap {:keys :transformation.select-keys/keys}]
    {:roaming.task/task-bundle-name :transformation/select-keys
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "tan"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.tan/target-key-path
              :dst :transformation.tan/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/tan
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "cos"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.cos/target-key-path
              :dst :transformation.cos/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/cos
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "lessThan"
  [{:keys [params] :as task}]
  (let [kmap {:leftPath :filter.less-than-pred/left-key-path
              :rightConstant :filter.less-than-pred/right-constant
              :negated :filter.less-than-pred/negated?
              :throwOnFalse :filter.less-than-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/less-than-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "stringEndsWith"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.string-ends-with-pred/target-key-path
              :substring :filter.string-ends-with-pred/substr-constant
              :negated :filter.string-ends-with-pred/negated?
              :throwOnFalse :filter.string-ends-with-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/string-ends-with-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "mod"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.mod/target-key-path
              :constant :transformation.mod/constant
              :operationPath :transformation.mod/operation-key-path
              :dst :transformation.mod/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/mod
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "last"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.last/target-key-path
              :dst :transformation.last/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/last
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "explode"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.explode/target-key-path
              :dst :transformation.explode/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/explode
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "move"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.move/target-path
              :dst :transformation.move/result-path}]
    {:roaming.task/task-bundle-name :transformation/move
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "customFilter"
  [{:keys [params] :as task}]
  (let [kmap {:code :filter.custom-javascript-pred/code
              :negated :filter.custom-javascript-pred/negated?
              :throwOnFalse :filter.custom-javascript-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/custom-javascript-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "abs"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.abs/target-key-path
              :dst :transformation.abs/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/abs
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "extractRegexMatch"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.extract-regex-match/target-key-path
              :pattern :transformation.extract-regex-match/pattern
              :dst :transformation.extract-regex-match/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/extract-regex-match
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "expm1"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.expm1/target-key-path
              :dst :transformation.expm1/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/expm1
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "join"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.join/target-key-path
              :join :transformation.join/join
              :dst :transformation.join/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/join
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "divide"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.divide/target-key-path
              :constant :transformation.divide/constant
              :operationPath :transformation.divide/operation-key-path
              :dst :transformation.divide/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/divide
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "takeNth"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.take-nth/target-key-path
              :index :transformation.take-nth/index
              :dst :transformation.take-nth/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/take-nth
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "randomUUID"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.random-uuid/target-key-path}]
    {:roaming.task/task-bundle-name :transformation/random-uuid
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isEmpty"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.empty-pred/target-key-path
              :negated :filter.empty-pred/negated?
              :throwOnFalse :filter.empty-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/empty-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "notEquals"
  [{:keys [params] :as task}]
  (let [kmap {:leftPath :filter.not-equals-pred/left-key-path
              :rightConstant :filter.not-equals-pred/right-constant
              :negated :filter.not-equals-pred/negated?
              :throwOnFalse :filter.not-equals-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/not-equals-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isFalse"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.false-pred/target-key-path
              :negated :filter.false-pred/negated?
              :throwOnFalse :filter.false-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/false-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "sinh"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.sinh/target-key-path
              :dst :transformation.sinh/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/sinh
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "round"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.round/target-key-path
              :dst :transformation.round/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/round
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "assocIn"
  [{:keys [params] :as task}]
  (let [kmap {:dst :transformation.assoc-in/result-path
              :value :transformation.assoc-in/value
              :valuePath :transformation.assoc-in/value-key-path}]
    {:roaming.task/task-bundle-name :transformation/assoc-in
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "merge"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.merge/target-key-path
              :constant :transformation.merge/rhs-map
              :operationPath :transformation.merge/map-key-path
              :dst :transformation.merge/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/merge
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "asin"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.asin/target-key-path
              :dst :transformation.asin/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/asin
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "equals"
  [{:keys [params] :as task}]
  (let [kmap {:leftPath :filter.equals-pred/left-key-path
              :constant :unformed.filter.equals-pred/right-constant
              :negated :filter.equals-pred/negated?
              :throwOnFalse :filter.equals-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/equals-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "tanh"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.tanh/target-key-path
              :dst :transformation.tanh/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/tanh
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "toRadians"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.to-radians/target-key-path
              :dst :transformation.to-radians/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/to-radians
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "trimr"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.trimr/target-key-path
              :dst :transformation.trimr/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/trimr
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "stringStartsWith"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.string-starts-with-pred/target-key-path
              :negated :filter.string-starts-with-pred/negated?
              :substring :filter.string-starts-with-pred/substr-constant
              :throwOnFalse :filter.string-starts-with-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/string-starts-with-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "splitBy"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.split-by/target-key-path
              :pattern :transformation.split-by/pattern
              :dst :transformation.split-by/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/split-by
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "first"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.first/target-key-path
              :dst :transformation.first/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/first
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "isString"
  [{:keys [params] :as task}]
  (let [kmap {:src :filter.string-pred/target-key-path
              :negated :filter.string-pred/negated?
              :throwOnFalse :filter.string-pred/throw-on-false?}]
    {:roaming.task/task-bundle-name :filter/string-pred
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "partitionAll"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.partition-all/target-key-path
              :n :transformation.partition-all/n
              :nPath :transformation.partition-all/n-key-path
              :dst :transformation.partition-all/result-key-path
              :step :transformation.partition-all/step}]
    {:roaming.task/task-bundle-name :transformation/partition-all
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmethod canonicalize "acos"
  [{:keys [params] :as task}]
  (let [kmap {:src :transformation.acos/target-key-path
              :dst :transformation.acos/result-key-path}]
    {:roaming.task/task-bundle-name :transformation/acos
     :task/bundle-version 0
     :params (rename-keys params kmap)}))

(defmulti canonicalize-aggregation
  (fn [aggregation]
    (:operation aggregation)))

(defn agg-specific [f kw]
  (keyword (str "aggregation." f) (name kw)))

(defn unformed-specific [f kw]
  (keyword (str "unformed.aggregation." f) (name kw)))

(defn fixed-params [f aggregation]
  {(agg-specific f :window-key) (:timestampKey aggregation)
   (unformed-specific f :range-duration) (:rangeDuration aggregation)
   (unformed-specific f :range-units) (:rangeUnits aggregation)})

(defn- sliding-params [f aggregation]
  {(agg-specific f :window-key) (:timestampKey aggregation)
   (unformed-specific f :range-duration) (:rangeDuration aggregation)
   (unformed-specific f :range-units) (:rangeUnits aggregation)
   (unformed-specific f :slide-duration) (:slideDuration aggregation)
   (unformed-specific f :slide-units) (:slideUnits aggregation)})

(defn- session-params [f aggregation]
  {(agg-specific f :window-key) (:timestampKey aggregation)
   (agg-specific f :session-key) (:sessionKey aggregation)
   (unformed-specific f :timeout-gap-duration) (:timeoutGapDuration aggregation)
   (unformed-specific f :timeout-gap-units) (:timeoutGapUnits aggregation)})

(defmethod canonicalize-aggregation "count"
  [aggregation]
  (let [base {:unformed.task-params/name :aggregation/count
              :unformed.aggregation/window-type (:windowType aggregation)
              :unformed.aggregation/window-id (u/make-random-uuid)
              :aggregation/window-name (:name aggregation)}]
    (case (:windowType aggregation)
      :global base
      :fixed (into base (fixed-params "count" aggregation))
      :sliding (into base (sliding-params "count" aggregation))
      :session (into base (session-params "count" aggregation)))))

(defmethod canonicalize-aggregation "sum"
  [aggregation]
  (let [base {:unformed.task-params/name :aggregation/sum
              :unformed.aggregation/window-type (:windowType aggregation)
              :unformed.aggregation/window-id (u/make-random-uuid)
              :aggregation/window-name (:name aggregation)}]))

(defmethod canonicalize-aggregation "min"
  [aggregation]
  (let [base {:unformed.task-params/name :aggregation/min
              :unformed.aggregation/window-type (:windowType aggregation)
              :unformed.aggregation/window-id (u/make-random-uuid)
              :aggregation/window-name (:name aggregation)}]))

(defmethod canonicalize-aggregation "max"
  [aggregation]
  (let [base {:unformed.task-params/name :aggregation/max
              :unformed.aggregation/window-type (:windowType aggregation)
              :unformed.aggregation/window-id (u/make-random-uuid)
              :aggregation/window-name (:name aggregation)}]))

(defmethod canonicalize-aggregation "average"
  [aggregation]
  (let [base {:unformed.task-params/name :aggregation/average
              :unformed.aggregation/window-type (:windowType aggregation)
              :unformed.aggregation/window-id (u/make-random-uuid)
              :aggregation/window-name (:name aggregation)}]))

(defmethod canonicalize "materializeView"
  [{:keys [params aggregations] :as task}]
  {:roaming.task/task-bundle-name :aggregation/multi
   :task/bundle-version 0
   :params
   (cond-> {}
     true
     (assoc :unformed.aggregation.multi/bundles (map canonicalize-aggregation aggregations))

     (:groupBy params)
     (assoc :unformed.aggregation.multi/group-by (:groupBy params)))})
