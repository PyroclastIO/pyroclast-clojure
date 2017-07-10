(ns pyroclast-clojure.roaming.coerce
  (:require [pyroclast-clojure.util :as u]))

(defn parse-vals
  ([service parse-map]
   (parse-vals service parse-map {}))
  ([service parse-map {:keys [nilable?]}]
   (u/integrate-params service :transformation/parse-vals 0
                       {:unformed.transformation.parse-vals/parse-map parse-map
                        :transformation.parse-vals/tolerate-nils? nilable?})))
