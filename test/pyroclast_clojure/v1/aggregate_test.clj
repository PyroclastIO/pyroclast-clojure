(ns pyroclast-clojure.v1.aggregate-test
  (:require [pyroclast-clojure.v1.client :as client]
            [pyroclast-clojure.util :as u]
            [clojure.test :refer [is deftest]]))

(deftest ^:topic agg-tests
  (let [config (:aggregate-client-config (u/load-config "config.edn"))]
    ;; Fetch aggregates for a deployment
    (is @(client/deployment-fetch-aggregates config))))


