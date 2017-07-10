(ns pyroclast-clojure.v1.roaming.string
  (:refer-clojure :exclude [replace])
  (:require [pyroclast-clojure.util :as u]))

(defn capitalize
  ([service src]
   (capitalize service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/capitalize 0
                       {:transformation.capitalize/target-key-path (u/mvec src)
                        :transformation.capitalize/result-key-path (u/mvec dst)})))

(defn trim
  ([service src]
   (trim service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/trim 0
                       {:transformation.trim/target-key-path (u/mvec src)
                        :transformation.trim/result-key-path (u/mvec dst)})))

(defn triml
  ([service src]
   (triml service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/triml 0
                       {:transformation.triml/target-key-path (u/mvec src)
                        :transformation.triml/result-key-path (u/mvec dst)})))

(defn trimr
  ([service src]
   (trimr service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/trimr 0
                       {:transformation.trimr/target-key-path (u/mvec src)
                        :transformation.trimr/result-key-path (u/mvec dst)})))

(defn trim-newline
  ([service src]
   (trim-newline service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/trim-newline 0
                       {:transformation.trim-newline/target-key-path (u/mvec src)
                        :transformation.trim-newline/result-key-path (u/mvec dst)})))

(defn string-reverse
  ([service src]
   (string-reverse service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/string-reverse 0
                       {:transformation.string-reverse/target-key-path (u/mvec src)
                        :transformation.string-reverse/result-key-path (u/mvec dst)})))

(defn upper-case
  ([service src]
   (upper-case service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/upper-case 0
                       {:transformation.upper-case/target-key-path (u/mvec src)
                        :transformation.upper-case/result-key-path (u/mvec dst)})))

(defn lower-case
  ([service src]
   (lower-case service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/lower-case 0
                       {:transformation.lower-case/target-key-path (u/mvec src)
                        :transformation.lower-case/result-key-path (u/mvec dst)})))

(defn split-lines
  ([service src]
   (split-lines service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/split-lines 0
                       {:transformation.split-lines/target-key-path (u/mvec src)
                        :transformation.split-lines/result-key-path (u/mvec dst)})))

(defn split-whitespace
  ([service src]
   (split-whitespace service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/split-whitespace 0
                       {:transformation.split-whitespace/target-key-path (u/mvec src)
                        :transformation.split-whitespace/result-key-path (u/mvec dst)})))

(defn replace
  ([service src pattern replacement]
   (replace service src pattern replacement {}))
  ([service src pattern replacement {:keys [dst]}]
   (u/integrate-params service :transformation/replace 0
                       {:transformation.replace/target-key-path (u/mvec src)
                        :transformation.replace/result-key-path (u/mvec dst)
                        :transformation.replace/pattern pattern
                        :transformation.replace/replacement replacement})))

(defn replace-first
  ([service src pattern replacement]
   (replace-first service src pattern replacement {}))
  ([service src pattern replacement {:keys [dst]}]
   (u/integrate-params service :transformation/replace-first 0
                       {:transformation.replace-first/target-key-path (u/mvec src)
                        :transformation.replace-first/result-key-path (u/mvec dst)
                        :transformation.replace-first/pattern pattern
                        :transformation.replace-first/replacement replacement})))

(defn concat-keys
  ([service src separator dst]
   (u/integrate-params service :transformation/concat-keys 0
                       {:transformation.concat-keys/multi-target-key-path (u/mvec src)
                        :transformation.concat-keys/result-key-path (u/mvec dst)
                        :transformation.concat-keys/separator separator})))

(defn extract-regex-match
  ([service src pattern]
   (extract-regex-match service src pattern {}))
  ([service src pattern {:keys [dst]}]
   (u/integrate-params service :transformation/extract-regex-match 0
                       {:transformation.extract-regex-match/target-key-path (u/mvec src)
                        :transformation.extract-regex-match/result-key-path (u/mvec dst)
                        :transformation.extract-regex-match/pattern pattern})))

(defn extract-regex-matches
  ([service src pattern]
   (extract-regex-matches service src pattern {}))
  ([service src pattern {:keys [dst]}]
   (u/integrate-params service :transformation/extract-regex-matches 0
                       {:transformation.extract-regex-matches/target-key-path (u/mvec src)
                        :transformation.extract-regex-matches/result-key-path (u/mvec dst)
                        :transformation.extract-regex-matches/pattern pattern})))

(defn format-string
  ([service src-keys string-format dst]
   (u/integrate-params service :transformation/format-string 0
                       {:transformation.format-string/multi-target-key-path (mapv u/mvec src-keys)
                        :transformation.format-string/result-key-path (u/mvec dst)
                        :transformation.format-string/format string-format})))

(defn join
  ([service src separator]
   (join service src separator {}))
  ([service src separator {:keys [dst]}]
   (u/integrate-params service :transformation/join 0
                       {:transformation.join/target-key-path (u/mvec src)
                        :transformation.join/result-key-path (u/mvec dst)
                        :transformation.join/join separator})))
