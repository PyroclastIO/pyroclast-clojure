(ns pyroclast-clojure.util)

(defn conjv [xs & more-xs]
  (apply (partial (fnil conj []) xs) more-xs))

(defn mvec [x-or-xs]
  (cond (vector? x-or-xs) x-or-xs
        (nil? x-or-xs) x-or-xs
        :else (vector x-or-xs)))

(defn remove-nil-map-vals [m]
  (into {} (keep (fn [[k v]] (when (not (nil? v)) [k v]))) m))

(defn make-random-uuid []
  #?(:clj (java.util.UUID/randomUUID)
     :cljs (cljs.core/random-uuid)))

(defn integrate-params [service bundle-name version params-map]
  (update service :roaming.service/tasks conjv
          {:roaming.task/task-bundle-name bundle-name
           :task/bundle-version version
           :params (remove-nil-map-vals params-map)}))

#?(:clj 
   (defn load-config [file-path]
     (read-string (slurp (clojure.java.io/resource file-path)))))
