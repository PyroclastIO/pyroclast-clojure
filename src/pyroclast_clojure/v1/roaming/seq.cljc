(ns pyroclast-clojure.v1.roaming.seq
  (:refer-clojure :exclude [first second rest butlast nth rand-nth
                            count reverse sort shuffle frequencies
                            seq distinct take take-nth drop drop-last
                            assoc-in dissoc zipmap select-keys merge
                            partition partition-all last keys vals repeat])
  (:require [pyroclast-clojure.util :as u]))

(defn first
  ([service src]
   (first service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/first 0
                       {:transformation.first/target-key-path (u/mvec src)
                        :transformation.first/result-key-path (u/mvec dst)})))

(defn second
  ([service src]
   (second service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/second 0
                       {:transformation.second/target-key-path (u/mvec src)
                        :transformation.second/result-key-path (u/mvec dst)})))

(defn rest
  ([service src]
   (rest service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/rest 0
                       {:transformation.rest/target-key-path (u/mvec src)
                        :transformation.rest/result-key-path (u/mvec dst)})))

(defn last
  ([service src]
   (last service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/last 0
                       {:transformation.last/target-key-path (u/mvec src)
                        :transformation.last/result-key-path (u/mvec dst)})))

(defn butlast
  ([service src]
   (butlast service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/butlast 0
                       {:transformation.butlast/target-key-path (u/mvec src)
                        :transformation.butlast/result-key-path (u/mvec dst)})))

(defn count
  ([service src]
   (count service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/count 0
                       {:transformation.count/target-key-path (u/mvec src)
                        :transformation.count/result-key-path (u/mvec dst)})))

(defn reverse
  ([service src]
   (reverse service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/reverse 0
                       {:transformation.reverse/target-key-path (u/mvec src)
                        :transformation.reverse/result-key-path (u/mvec dst)})))

(defn sort
  ([service src]
   (sort service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/sort 0
                       {:transformation.sort/target-key-path (u/mvec src)
                        :transformation.sort/result-key-path (u/mvec dst)})))

(defn shuffle
  ([service src]
   (shuffle service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/shuffle 0
                       {:transformation.shuffle/target-key-path (u/mvec src)
                        :transformation.shuffle/result-key-path (u/mvec dst)})))

(defn frequencies
  ([service src]
   (frequencies service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/frequencies 0
                       {:transformation.frequencies/target-key-path (u/mvec src)
                        :transformation.frequencies/result-key-path (u/mvec dst)})))

(defn explode
  ([service src]
   (explode service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/explode 0
                       {:transformation.explode/target-key-path (u/mvec src)
                        :transformation.explode/result-key-path (u/mvec dst)})))

(defn keys
  ([service src]
   (keys service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/keys 0
                       {:transformation.keys/target-key-path (u/mvec src)
                        :transformation.keys/result-key-path (u/mvec dst)})))

(defn vals
  ([service src]
   (vals service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/vals 0
                       {:transformation.vals/target-key-path (u/mvec src)
                        :transformation.vals/result-key-path (u/mvec dst)})))

(defn seq
  ([service src]
   (seq service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/seq 0
                       {:transformation.seq/target-key-path (u/mvec src)
                        :transformation.seq/result-key-path (u/mvec dst)})))

(defn distinct
  ([service src]
   (distinct service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/distinct 0
                       {:transformation.distinct/target-key-path (u/mvec src)
                        :transformation.distinct/result-key-path (u/mvec dst)})))

(defn take
  ([service src n]
   (take service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/take 0
                       {:transformation.take/target-key-path (u/mvec src)
                        :transformation.take/result-key-path (u/mvec dst)
                        :transformation.take/index n})))

(defn take-nth
  ([service src n]
   (take-nth service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/take-nth 0
                       {:transformation.take-nth/target-key-path (u/mvec src)
                        :transformation.take-nth/result-key-path (u/mvec dst)
                        :transformation.take-nth/index n})))

(defn drop
  ([service src n]
   (drop service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/drop 0
                       {:transformation.drop/target-key-path (u/mvec src)
                        :transformation.drop/result-key-path (u/mvec dst)
                        :unformed.transformation.drop/index n})))

(defn drop-last
  ([service src n]
   (drop-last service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/drop-last 0
                       {:transformation.drop-last/target-key-path (u/mvec src)
                        :transformation.drop-last/result-key-path (u/mvec dst)
                        :unformed.transformation.drop-last/index n})))

(defn nth
  ([service src n]
   (nth service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/nth 0
                       {:transformation.nth/target-key-path (u/mvec src)
                        :transformation.nth/result-key-path (u/mvec dst)
                        :unformed.transformation.nth/index n})))

(defn rand-nth
  ([service src]
   (rand-nth service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/rand-nth 0
                       {:transformation.rand-nth/target-key-path (u/mvec src)
                        :transformation.rand-nth/result-key-path (u/mvec dst)})))

(defn move
  ([service src]
   (move service src {}))
  ([service src dst]
   (u/integrate-params service :transformation/move 0
                       {:transformation.move/target-path src
                        :transformation.move/result-path dst})))

(defn assoc-in
  ([service dst value]
   (u/integrate-params service :transformation/assoc-in 0
                       {:transformation.assoc-in/result-path dst
                        :unformed.transformation.assoc-in/value value})))

(defn dynamic-assoc-in
  ([service dst operation-path]
   (u/integrate-params service :transformation/assoc-in 0
                       {:transformation.assoc-in/result-path dst
                        :transformation.assoc-in/value-key-path (u/mvec operation-path)})))

(defn assoc-under
  ([service dst]
   (u/integrate-params service :transformation/assoc-under 0
                       {:transformation.assoc-under/target-key dst})))

(defn dissoc
  ([service ks]
   (u/integrate-params service :transformation/dissoc 0
                       {:transformation.dissoc/target-keys ks})))

(defn zipmap
  ([service src ks]
   (zipmap service src ks {}))
  ([service src ks {:keys [dst]}]
   (u/integrate-params service :transformation/zipmap 0
                       {:transformation.zipmap/target-key-path (u/mvec src)
                        :transformation.zipmap/result-key-path (u/mvec dst)
                        :unformed.transformation.zipmap/keys ks})))

(defn dynamic-zipmap
  ([service src operation-path]
   (dynamic-zipmap service src operation-path {}))
  ([service src operation-path {:keys [dst]}]
   (u/integrate-params service :transformation/zipmap 0
                       {:transformation.zipmap/target-key-path (u/mvec src)
                        :transformation.zipmap/result-key-path (u/mvec dst)
                        :transformation.zipmap/keys-key-path (u/mvec operation-path)})))

(defn repeat
  ([service src n]
   (repeat service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/repeat 0
                       {:transformation.repeat/target-key-path (u/mvec src)
                        :transformation.repeat/result-key-path (u/mvec dst)
                        :unformed.transformation.repeat/n n})))

(defn dynamic-repeat
  ([service src operation-path]
   (dynamic-repeat service src operation-path {}))
  ([service src operation-path {:keys [dst]}]
   (u/integrate-params service :transformation/repeat 0
                       {:transformation.repeat/target-key-path (u/mvec src)
                        :transformation.repeat/result-key-path (u/mvec dst)
                        :transformation.repeat/n-key-path (u/mvec operation-path)})))

(defn select-keys
  ([service ks]
   (u/integrate-params service :transformation/select-keys 0
                       {:unformed.transformation.select-keys/keys ks})))

(defn merge
  ([service src m]
   (merge service src m {}))
  ([service src m {:keys [dst]}]
   (u/integrate-params service :transformation/merge 0
                       {:transformation.merge/target-key-path (u/mvec src)
                        :transformation.merge/result-key-path (u/mvec dst)
                        :unformed.transformation.merge/rhs-map m})))

(defn dynamic-merge
  ([service src operation-path]
   (dynamic-merge service src operation-path {}))
  ([service src operation-path {:keys [dst]}]
   (u/integrate-params service :transformation/merge 0
                       {:transformation.merge/target-key-path (u/mvec src)
                        :transformation.merge/result-key-path (u/mvec dst)
                        :transformation.merge/map-key-path (u/mvec operation-path)})))

(defn partition
  ([service src n]
   (partition service src n {}))
  ([service src n {:keys [dst step pad]}]
   (u/integrate-params service :transformation/partition 0
                       {:transformation.partition/target-key-path (u/mvec src)
                        :transformation.partition/result-key-path (u/mvec dst)
                        :unformed.transformation.partition/n n
                        :unformed.transformation.partition/step step
                        :unformed.transformation.partition/pad pad})))

(defn dynamic-partition
  ([service src operation-path]
   (dynamic-partition service src operation-path {}))
  ([service src operation-path {:keys [dst step pad]}]
   (u/integrate-params service :transformation/partition 0
                       {:transformation.partition/target-key-path (u/mvec src)
                        :transformation.partition/result-key-path (u/mvec dst)
                        :transformation.partition/n-key-path (u/mvec operation-path)
                        :unformed.transformation.partition/step step
                        :unformed.transformation.partition/pad pad})))

(defn partition-all
  ([service src n]
   (partition-all service src n {}))
  ([service src n {:keys [dst step pad]}]
   (u/integrate-params service :transformation/partition-all 0
                       {:transformation.partition-all/target-key-path (u/mvec src)
                        :transformation.partition-all/result-key-path (u/mvec dst)
                        :unformed.transformation.partition-all/n n
                        :unformed.transformation.partition-all/step step
                        :unformed.transformation.partition-all/pad pad})))

(defn dynamic-partition-all
  ([service src operation-path]
   (dynamic-partition-all service src operation-path {}))
  ([service src operation-path {:keys [dst step pad]}]
   (u/integrate-params service :transformation/partition-all 0
                       {:transformation.partition-all/target-key-path (u/mvec src)
                        :transformation.partition-all/result-key-path (u/mvec dst)
                        :transformation.partition-all/n-key-path (u/mvec operation-path)
                        :unformed.transformation.partition-all/step step
                        :unformed.transformation.partition-all/pad pad})))
