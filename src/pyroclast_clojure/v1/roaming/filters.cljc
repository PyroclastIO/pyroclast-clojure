(ns pyroclast-clojure.roaming.filters
  (:refer-clojure :exclude [coll? distinct? empty? even? => > >=
                            < <= = not= integer? map? neg? nil? string?
                            number? odd? pos? sequential? true? false? zero?])
  (:require [clojure.core :as cc]
            [pyroclast-clojure.util :as u]))

(defn coll?
  ([service src]
   (coll? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/coll-pred 0
                       {:filter.coll-pred/target-key-path (u/mvec src)
                        :filter.coll-pred/negated? negated?
                        :filter.coll-pred/throw-on-false? throw-on-false?})))

(defn distinct?
  ([service src]
   (distinct? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/distinct-pred 0
                       {:filter.distinct-pred/target-key-path (u/mvec src)
                        :filter.distinct-pred/negated? negated?
                        :filter.distinct-pred/throw-on-false? throw-on-false?})))

(defn empty?
  ([service src]
   (empty? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/empty-pred 0
                       {:filter.empty-pred/target-key-path (u/mvec src)
                        :filter.empty-pred/negated? negated?
                        :filter.empty-pred/throw-on-false? throw-on-false?})))

(defn even?
  ([service src]
   (even? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/even-pred 0
                       {:filter.even-pred/target-key-path (u/mvec src)
                        :filter.even-pred/negated? negated?
                        :filter.even-pred/throw-on-false? throw-on-false?})))

(defn odd?
  ([service src]
   (odd? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/odd-pred 0
                       {:filter.odd-pred/target-key-path (u/mvec src)
                        :filter.odd-pred/negated? negated?
                        :filter.odd-pred/throw-on-false? throw-on-false?})))

(defn =
  ([service left-src right-constant]
   (= service left-src right-constant {}))
  ([service left-src right-constant {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/equals-pred 0
                       {:filter.equals-pred/left-key-path (u/mvec left-src)
                        :unformed.filter.equals-pred/right-constant right-constant
                        :filter.equals-pred/negated? negated?
                        :filter.equals-pred/throw-on-false? throw-on-false?})))

(defn not=
  ([service left-src right-constant]
   (not= service left-src right-constant {}))
  ([service left-src right-constant {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/not-equals-pred 0
                       {:filter.not-equals-pred/left-key-path (u/mvec left-src)
                        :unformed.filter.not-equals-pred/right-constant right-constant
                        :filter.not-equals-pred/negated? negated?
                        :filter.not-equals-pred/throw-on-false? throw-on-false?})))

(defn <
  ([service left-src right-constant]
   (< service left-src right-constant {}))
  ([service left-src right-constant {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/less-than-pred 0
                       {:filter.less-than-pred/left-key-path (u/mvec left-src)
                        :unformed.filter.less-than-pred/right-constant right-constant
                        :filter.less-than-pred/negated? negated?
                        :filter.less-than-pred/throw-on-false? throw-on-false?})))

(defn <=
  ([service left-src right-constant]
   (<= service left-src right-constant {}))
  ([service left-src right-constant {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/less-than-or-equal-to-pred 0
                       {:filter.less-than-or-equal-to-pred/left-key-path (u/mvec left-src)
                        :unformed.filter.less-than-or-equal-to-pred/right-constant right-constant
                        :filter.less-than-or-equal-to-pred/negated? negated?
                        :filter.less-than-or-equal-to-pred/throw-on-false? throw-on-false?})))

(defn >
  ([service left-src right-constant]
   (> service left-src right-constant {}))
  ([service left-src right-constant {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/greater-than-pred 0
                       {:filter.greater-than-pred/left-key-path (u/mvec left-src)
                        :unformed.filter.greater-than-pred/right-constant right-constant
                        :filter.greater-than-pred/negated? negated?
                        :filter.greater-than-pred/throw-on-false? throw-on-false?})))

(defn >=
  ([service left-src right-constant]
   (>= service left-src right-constant {}))
  ([service left-src right-constant {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/greater-than-or-equal-to-pred 0
                       {:filter.greater-than-or-equal-to-pred/left-key-path (u/mvec left-src)
                        :unformed.filter.greater-than-or-equal-to-pred/right-constant right-constant
                        :filter.greater-than-or-equal-to-pred/negated? negated?
                        :filter.greater-than-or-equal-to-pred/throw-on-false? throw-on-false?})))

(defn pos?
  ([service src]
   (pos? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/pos-pred 0
                       {:filter.pos-pred/target-key-path (u/mvec src)
                        :filter.pos-pred/negated? negated?
                        :filter.pos-pred/throw-on-false? throw-on-false?})))

(defn neg?
  ([service src]
   (neg? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/neg-pred 0
                       {:filter.neg-pred/target-key-path (u/mvec src)
                        :filter.neg-pred/negated? negated?
                        :filter.neg-pred/throw-on-false? throw-on-false?})))

(defn integer?
  ([service src]
   (integer? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/integer-pred 0
                       {:filter.integer-pred/target-key-path (u/mvec src)
                        :filter.integer-pred/negated? negated?
                        :filter.integer-pred/throw-on-false? throw-on-false?})))

(defn map?
  ([service src]
   (map? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/map-pred 0
                       {:filter.map-pred/target-key-path (u/mvec src)
                        :filter.map-pred/negated? negated?
                        :filter.map-pred/throw-on-false? throw-on-false?})))

(defn nil?
  ([service src]
   (nil? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/nil-pred 0
                       {:filter.nil-pred/target-key-path (u/mvec src)
                        :filter.nil-pred/negated? negated?
                        :filter.nil-pred/throw-on-false? throw-on-false?})))

(defn number?
  ([service src]
   (number? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/number-pred 0
                       {:filter.number-pred/target-key-path (u/mvec src)
                        :filter.number-pred/negated? negated?
                        :filter.number-pred/throw-on-false? throw-on-false?})))

(defn sequential?
  ([service src]
   (sequential? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/sequential-pred 0
                       {:filter.sequential-pred/target-key-path (u/mvec src)
                        :filter.sequential-pred/negated? negated?
                        :filter.sequential-pred/throw-on-false? throw-on-false?})))

(defn true?
  ([service src]
   (true? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/true-pred 0
                       {:filter.true-pred/target-key-path (u/mvec src)
                        :filter.true-pred/negated? negated?
                        :filter.true-pred/throw-on-false? throw-on-false?})))

(defn false?
  ([service src]
   (false? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/false-pred 0
                       {:filter.false-pred/target-key-path (u/mvec src)
                        :filter.false-pred/negated? negated?
                        :filter.false-pred/throw-on-false? throw-on-false?})))

(defn zero?
  ([service src]
   (zero? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/zero-pred 0
                       {:filter.zero-pred/target-key-path (u/mvec src)
                        :filter.zero-pred/negated? negated?
                        :filter.zero-pred/throw-on-false? throw-on-false?})))

(defn in?
  ([service target constant]
   (in? service target constant {}))
  ([service target constant {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/in-pred 0
                       {:filter.in-pred/coll-key-path (u/mvec target)
                        :unformed.filter.in-pred/search-value-constant constant
                        :filter.in-pred/negated? negated?
                        :filter.in-pred/throw-on-false? throw-on-false?})))

(defn string-blank?
  ([service src]
   (string-blank? service src {}))
  ([service src {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/string-blank-pred 0
                       {:filter.string-blank-pred/target-key-path (u/mvec src)
                        :filter.string-blank-pred/negated? negated?
                        :filter.string-blank-pred/throw-on-false? throw-on-false?})))

(defn string-starts-with?
  ([service src const]
   (string-starts-with? service src const {}))
  ([service src const {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/string-starts-with-pred 0
                       {:filter.string-starts-with-pred/target-key-path (u/mvec src)
                        :filter.string-starts-with-pred/substr-constant const
                        :filter.string-starts-with-pred/negated? negated?
                        :filter.string-starts-with-pred/throw-on-false? throw-on-false?})))

(defn string-ends-with?
  ([service src const]
   (string-ends-with? service src const {}))
  ([service src const {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/string-ends-with-pred 0
                       {:filter.string-ends-with-pred/target-key-path (u/mvec src)
                        :filter.string-ends-with-pred/substr-constant const
                        :filter.string-ends-with-pred/negated? negated?
                        :filter.string-ends-with-pred/throw-on-false? throw-on-false?})))

(defn string-includes?
  ([service src const]
   (string-includes? service src const {}))
  ([service src const {:keys [negated? throw-on-false?] :or {negated? false throw-on-false? false}}]
   (u/integrate-params service :filter/string-includes-pred 0
                       {:filter.string-includes-pred/target-key-path (u/mvec src)
                        :filter.string-includes-pred/substr-constant const
                        :filter.string-includes-pred/negated? negated?
                        :filter.string-includes-pred/throw-on-false? throw-on-false?})))
