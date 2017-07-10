(ns pyroclast-clojure.v1.roaming.aggregations
  (:refer-clojure :exclude [count min max group-by])
  (:require [clojure.core :as cc]
            [pyroclast-clojure.util :as u]))

(defn globally-windowed []
  {:kind :global})

(defn fixed-windows-of [duration units window-key]
  {:kind :fixed
   :range-duration duration
   :range-units units
   :window-key window-key})

(defn sliding-windows-of [range-duration range-units slide-duration slide-units window-key]
  {:kind :sliding
   :range-duration range-duration
   :range-units range-units
   :slide-duration slide-duration
   :slide-units slide-units
   :window-key window-key})

(defn session-windows-of [session-key timeout-duration timeout-units window-key]
  {:kind :session
   :timeout-gap-duration timeout-duration
   :timeout-gap-units timeout-units
   :session-key session-key
   :window-key window-key})

(defn agg-specific [f kw]
  (keyword (str "aggregation." f) (name kw)))

(defn unformed-specific [f kw]
  (keyword (str "unformed.aggregation." f) (name kw)))

(defn- fixed-params [f window]
  {(agg-specific f :window-key) (:window-key window)
   (unformed-specific f :range-duration) (:range-duration window)
   (unformed-specific f :range-units) (:range-units window)})

(defn- sliding-params [f window]
  {(agg-specific f :window-key) (:window-key window)
   (unformed-specific f :range-duration) (:range-duration window)
   (unformed-specific f :range-units) (:range-units window)
   (unformed-specific f :slide-duration) (:slide-duration window)
   (unformed-specific f :slide-units) (:slide-units window)})

(defn- session-params [f window]
  {(agg-specific f :window-key) (:window-key window)
   (agg-specific f :session-key) (:session-key window)
   (unformed-specific f :timeout-gap-duration) (:timeout-gap-duration window)
   (unformed-specific f :timeout-gap-units) (:timeout-gap-units window)})

(defn sum [aggregation-name field window]
  (let [base {:unformed.task-params/name :aggregation/sum
              :unformed.aggregation/window-type (:kind window)
              :unformed.aggregation/window-id (u/make-random-uuid)
              :aggregation/window-name aggregation-name
              :aggregation.sum/field field}]
    (case (:kind window)
      :global base
      :fixed (into base (fixed-params "sum" window))
      :sliding (into base (sliding-params "sum" window))
      :session (into base (session-params "sum" window)))))

(defn min [aggregation-name field window]
  (let [base {:unformed.task-params/name :aggregation/min
              :unformed.aggregation/window-type (:kind window)
              :unformed.aggregation/window-id (u/make-random-uuid)
              :aggregation/window-name aggregation-name
              :aggregation.min/field field}]
    (case (:kind window)
      :global base
      :fixed (into base (fixed-params "min" window))
      :sliding (into base (sliding-params "min" window))
      :session (into base (session-params "min" window)))))

(defn max [aggregation-name field window]
  (let [base {:unformed.task-params/name :aggregation/max
              :unformed.aggregation/window-type (:kind window)
              :unformed.aggregation/window-id (u/make-random-uuid)
              :aggregation/window-name aggregation-name
              :aggregation.max/field field}]
    (case (:kind window)
      :global base
      :fixed (into base (fixed-params "max" window))
      :sliding (into base (sliding-params "max" window))
      :session (into base (session-params "max" window)))))

(defn average [aggregation-name field window]
  (let [base {:unformed.task-params/name :aggregation/average
              :unformed.aggregation/window-type (:kind window)
              :unformed.aggregation/window-id (u/make-random-uuid)
              :aggregation/window-name aggregation-name
              :aggregation.average/field field}]
    (case (:kind window)
      :global base
      :fixed (into base (fixed-params "average" window))
      :sliding (into base (sliding-params "average" window))
      :session (into base (session-params "average" window)))))

(defn count [aggregation-name window]
  (let [base {:unformed.task-params/name :aggregation/count
              :unformed.aggregation/window-type (:kind window)
              :unformed.aggregation/window-id (u/make-random-uuid)
              :aggregation/window-name aggregation-name}]
    (case (:kind window)
      :global base
      :fixed (into base (fixed-params "count" window))
      :sliding (into base (sliding-params "count" window))
      :session (into base (session-params "count" window)))))

(defn aggregate-together
  ([service aggregates]
   (aggregate-together service aggregates nil))
  ([service aggregates group-by]
   (let [params (cond-> {:unformed.aggregation.multi/bundles aggregates}
                  group-by (assoc :unformed.aggregation.multi/group-by (u/mvec group-by)))]
     (u/integrate-params service :aggregation/multi 0 params))))
