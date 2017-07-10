(ns pyroclast-clojure.roaming.roaming.service
  (:require [cheshire.core :refer [generate-string]]))

(defn new-service []
  {:roaming.service/tasks []})

(defn to-manifest-file [service file-path]
  (spit file-path (generate-string service {:pretty true})))
