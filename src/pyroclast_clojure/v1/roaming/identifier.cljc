(ns pyroclast-clojure.v1.roaming.identifier
  (:refer-clojure :exclude [hash])
  (:require [pyroclast-clojure.util :as u]))

(defn random-uuid [service dst]
  (u/integrate-params service :transformation/random-uuid 0
                      {:transformation.random-uuid/target-key-path (u/mvec dst)}))

(defn hash [service dst]
  (u/integrate-params service :transformation/hash 0
                      {:transformation.hash/target-key-path (u/mvec dst)}))
