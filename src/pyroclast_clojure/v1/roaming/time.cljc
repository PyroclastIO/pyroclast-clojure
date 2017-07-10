(ns pyroclast-clojure.roaming.roaming.time
  (:require [pyroclast-clojure.util :as u]))

(defn parse-datetime
  ([service src datetime-format]
   (parse-datetime service src datetime-format {}))
  ([service src datetime-format {:keys [dst]}]
   (u/integrate-params service :transformation/parse-datetime 0
                       {:transformation.parse-datetime/target-key-path (u/mvec src)
                        :transformation.parse-datetime/result-key-path (u/mvec dst)
                        :transformation.parse-datetime/format datetime-format})))

(defn format-unix-ms-timestamp
  ([service src datetime-format]
   (format-unix-ms-timestamp service src datetime-format {}))
  ([service src datetime-format {:keys [dst]}]
   (u/integrate-params service :transformation/format-unix-ms-timestamp 0
                       {:transformation.format-unix-ms-timestamp/target-key-path (u/mvec src)
                        :transformation.format-unix-ms-timestamp/result-key-path (u/mvec dst)
                        :transformation.format-unix-ms-timestamp/format datetime-format})))
