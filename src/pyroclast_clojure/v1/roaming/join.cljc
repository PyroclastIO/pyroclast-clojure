(ns pyroclast-clojure.roaming.join
  (:require [pyroclast-clojure.util :as u]))

(defn join-by-key-static [service dst join-key records]
  (u/integrate-params service :transformation/join-by-key-static 0
                      {:transformation.join-by-key-static/result-key-path (u/mvec dst)
                       :transformation.join-by-key-static/join-key join-key
                       :transformation.join-by-key-static/static-records records}))

(defn join-by-key-s3 [service dst join-key {:keys [bucket file-key access-key secret-key]}]
  (u/integrate-params service :transformation/join-by-key-s3 0
                      {:transformation.join-by-key-s3/result-key-path (u/mvec dst)
                       :transformation.join-by-key-s3/join-key join-key
                       :transformation.join-by-key-s3/bucket bucket
                       :transformation.join-by-key-s3/key file-key
                       :transformation.join-by-key-s3/access-key access-key
                       :transformation.join-by-key-s3/secret-key secret-key}))
