(ns pyroclast-clojure.roaming.math
  (:refer-clojure :exclude [mod min max])
  (:require [pyroclast-clojure.util :as u]))

(defn plus
  ([service src n]
   (plus service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/plus 0
                       {:transformation.plus/target-key-path (u/mvec src)
                        :transformation.plus/result-key-path (u/mvec dst)
                        :unformed.transformation.plus/constant n})))

(defn dynamic-plus
  ([service src operation-key-path]
   (dynamic-plus service src operation-key-path {}))
  ([service src operation-key-path {:keys [dst]}]
   (u/integrate-params service :transformation/plus 0
                       {:transformation.plus/target-key-path (u/mvec src)
                        :transformation.plus/result-key-path (u/mvec dst)
                        :transformation.plus/operation-key-path (u/mvec operation-key-path)})))

(defn minus
  ([service src n]
   (minus service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/minus 0
                       {:transformation.minus/target-key-path (u/mvec src)
                        :transformation.minus/result-key-path (u/mvec dst)
                        :unformed.transformation.minus/constant n})))

(defn dynamic-minus
  ([service src operation-key-path]
   (dynamic-minus service src operation-key-path {}))
  ([service src operation-key-path {:keys [dst]}]
   (u/integrate-params service :transformation/minus 0
                       {:transformation.minus/target-key-path (u/mvec src)
                        :transformation.minus/result-key-path (u/mvec dst)
                        :transformation.minus/operation-key-path (u/mvec operation-key-path)})))

(defn times
  ([service src n]
   (times service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/times 0
                       {:transformation.times/target-key-path (u/mvec src)
                        :transformation.times/result-key-path (u/mvec dst)
                        :unformed.transformation.times/constant n})))

(defn dynamic-times
  ([service src operation-key-path]
   (dynamic-times service src operation-key-path {}))
  ([service src operation-key-path {:keys [dst]}]
   (u/integrate-params service :transformation/times 0
                       {:transformation.times/target-key-path (u/mvec src)
                        :transformation.times/result-key-path (u/mvec dst)
                        :transformation.times/operation-key-path (u/mvec operation-key-path)})))

(defn divide
  ([service src n]
   (divide service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/divide 0
                       {:transformation.divide/target-key-path (u/mvec src)
                        :transformation.divide/result-key-path (u/mvec dst)
                        :unformed.transformation.divide/constant n})))

(defn dynamic-divide
  ([service src operation-key-path]
   (dynamic-divide service src operation-key-path {}))
  ([service src operation-key-path {:keys [dst]}]
   (u/integrate-params service :transformation/divide 0
                       {:transformation.divide/target-key-path (u/mvec src)
                        :transformation.divide/result-key-path (u/mvec dst)
                        :transformation.divide/operation-key-path (u/mvec operation-key-path)})))

(defn mod
  ([service src n]
   (mod service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/mod 0
                       {:transformation.mod/target-key-path (u/mvec src)
                        :transformation.mod/result-key-path (u/mvec dst)
                        :unformed.transformation.mod/constant n})))

(defn dynamic-mod
  ([service src operation-key-path]
   (dynamic-mod src operation-key-path {}))
  ([service src operation-key-path {:keys [dst]}]
   (u/integrate-params service :transformation/mod 0
                       {:transformation.mod/target-key-path (u/mvec src)
                        :transformation.mod/result-key-path (u/mvec dst)
                        :transformation.mod/operation-key-path (u/mvec operation-key-path)})))

(defn quotient
  ([service src n]
   (quotient service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/quotient 0
                       {:transformation.quotient/target-key-path (u/mvec src)
                        :transformation.quotient/result-key-path (u/mvec dst)
                        :unformed.transformation.quotient/constant n})))

(defn dynamic-quotient
  ([service src operation-key-path]
   (dynamic-quotient service src operation-key-path {}))
  ([service src operation-key-path {:keys [dst]}]
   (u/integrate-params service :transformation/quotient 0
                       {:transformation.quotient/target-key-path (u/mvec src)
                        :transformation.quotient/result-key-path (u/mvec dst)
                        :transformation.quotient/operation-key-path (u/mvec operation-key-path)})))

(defn remainder
  ([service src n]
   (remainder service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/remainder 0
                       {:transformation.remainder/target-key-path (u/mvec src)
                        :transformation.remainder/result-key-path (u/mvec dst)
                        :unformed.transformation.remainder/constant n})))

(defn dynamic-remainder
  ([service src operation-key-path]
   (dynamic-remainder service src operation-key-path {}))
  ([service src operation-key-path {:keys [dst]}]
   (u/integrate-params service :transformation/remainder 0
                       {:transformation.remainder/target-key-path (u/mvec src)
                        :transformation.remainder/result-key-path (u/mvec dst)
                        :transformation.remainder/operation-key-path (u/mvec operation-key-path)})))

(defn pow
  ([service src n]
   (pow service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/pow 0
                       {:transformation.pow/target-key-path (u/mvec src)
                        :transformation.pow/result-key-path (u/mvec dst)
                        :unformed.transformation.pow/constant n})))

(defn dynamic-pow
  ([service src operation-key-path]
   (dynamic-pow service src operation-key-path {}))
  ([service src operation-key-path {:keys [dst]}]
   (u/integrate-params service :transformation/pow 0
                       {:transformation.pow/target-key-path (u/mvec src)
                        :transformation.pow/result-key-path (u/mvec dst)
                        :transformation.pow/operation-key-path (u/mvec operation-key-path)})))

(defn abs
  ([service src]
   (abs service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/abs 0
                       {:transformation.abs/target-key-path (u/mvec src)
                        :transformation.abs/result-key-path (u/mvec dst)})))

(defn cos
  ([service src]
   (cos service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/cos 0
                       {:transformation.cos/target-key-path (u/mvec src)
                        :transformation.cos/result-key-path (u/mvec dst)})))

(defn acos
  ([service src]
   (acos service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/acos 0
                       {:transformation.acos/target-key-path (u/mvec src)
                        :transformation.acos/result-key-path (u/mvec dst)})))

(defn cosh
  ([service src]
   (cosh service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/cosh 0
                       {:transformation.cosh/target-key-path (u/mvec src)
                        :transformation.cosh/result-key-path (u/mvec dst)})))

(defn sin
  ([service src]
   (sin service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/sin 0
                       {:transformation.sin/target-key-path (u/mvec src)
                        :transformation.sin/result-key-path (u/mvec dst)})))

(defn asin
  ([service src]
   (asin service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/asin 0
                       {:transformation.asin/target-key-path (u/mvec src)
                        :transformation.asin/result-key-path (u/mvec dst)})))

(defn sinh
  ([service src]
   (sinh service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/sinh 0
                       {:transformation.sinh/target-key-path (u/mvec src)
                        :transformation.sinh/result-key-path (u/mvec dst)})))

(defn tan
  ([service src]
   (tan service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/tan 0
                       {:transformation.tan/target-key-path (u/mvec src)
                        :transformation.tan/result-key-path (u/mvec dst)})))

(defn atan
  ([service src]
   (atan service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/atan 0
                       {:transformation.atan/target-key-path (u/mvec src)
                        :transformation.atan/result-key-path (u/mvec dst)})))

(defn tanh
  ([service src]
   (tanh service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/tanh 0
                       {:transformation.tanh/target-key-path (u/mvec src)
                        :transformation.tanh/result-key-path (u/mvec dst)})))

(defn sqrt
  ([service src]
   (sqrt service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/sqrt 0
                       {:transformation.sqrt/target-key-path (u/mvec src)
                        :transformation.sqrt/result-key-path (u/mvec dst)})))

(defn cbrt
  ([service src]
   (cbrt service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/cbrt 0
                       {:transformation.cbrt/target-key-path (u/mvec src)
                        :transformation.cbrt/result-key-path (u/mvec dst)})))

(defn exp
  ([service src]
   (exp service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/exp 0
                       {:transformation.exp/target-key-path (u/mvec src)
                        :transformation.exp/result-key-path (u/mvec dst)})))

(defn expm1
  ([service src]
   (expm1 service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/expm1 0
                       {:transformation.expm1/target-key-path (u/mvec src)
                        :transformation.expm1/result-key-path (u/mvec dst)})))

(defn floor
  ([service src]
   (floor service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/floor 0
                       {:transformation.floor/target-key-path (u/mvec src)
                        :transformation.floor/result-key-path (u/mvec dst)})))

(defn ceil
  ([service src]
   (ceil service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/ceil 0
                       {:transformation.ceil/target-key-path (u/mvec src)
                        :transformation.ceil/result-key-path (u/mvec dst)})))

(defn log
  ([service src]
   (log service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/log 0
                       {:transformation.log/target-key-path (u/mvec src)
                        :transformation.log/result-key-path (u/mvec dst)})))

(defn log10
  ([service src]
   (log10 service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/log10 0
                       {:transformation.log10/target-key-path (u/mvec src)
                        :transformation.log10/result-key-path (u/mvec dst)})))

(defn log1p
  ([service src]
   (log1p service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/log1p 0
                       {:transformation.log1p/target-key-path (u/mvec src)
                        :transformation.log1p/result-key-path (u/mvec dst)})))

(defn round
  ([service src]
   (round service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/round 0
                       {:transformation.round/target-key-path (u/mvec src)
                        :transformation.round/result-key-path (u/mvec dst)})))

(defn round-decimals
  ([service src n]
   (round-decimals service src n {}))
  ([service src n {:keys [dst]}]
   (u/integrate-params service :transformation/round-decimals 0
                       {:transformation.round-decimals/target-key-path (u/mvec src)
                        :transformation.round-decimals/result-key-path (u/mvec dst)
                        :unformed.transformation.round-decimals/n-decimals n})))

(defn to-degrees
  ([service src]
   (to-degrees service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/to-degrees 0
                       {:transformation.to-degrees/target-key-path (u/mvec src)
                        :transformation.to-degrees/result-key-path (u/mvec dst)})))

(defn to-radians
  ([service src]
   (to-radians service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/to-radians 0
                       {:transformation.to-radians/target-key-path (u/mvec src)
                        :transformation.to-radians/result-key-path (u/mvec dst)})))

(defn min
  ([service src]
   (min service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/min 0
                       {:transformation.min/target-key-path (u/mvec src)
                        :transformation.min/result-key-path (u/mvec dst)})))

(defn max
  ([service src]
   (max service src {}))
  ([service src {:keys [dst]}]
   (u/integrate-params service :transformation/max 0
                       {:transformation.max/target-key-path (u/mvec src)
                        :transformation.max/result-key-path (u/mvec dst)})))

(defn atan2
  ([service x y dst]
   (u/integrate-params service :transformation/atan2 0
                       {:unformed.transformation.atan2/x x
                        :unformed.transformation.atan2/y y
                        :transformation.atan2/result-key-path (u/mvec dst)})))

(defn atan2-dynamic
  ([service x-key y-key dst]
   (u/integrate-params service :transformation/atan2 0
                       {:transformation.atan2/x-key (u/mvec x-key)
                        :transformation.atan2/y-key (u/mvec y-key)
                        :transformation.atan2/result-key-path (u/mvec dst)})))

(defn hypot
  ([service x y dst]
   (u/integrate-params service :transformation/hypot 0
                       {:unformed.transformation.hypot/x x
                        :unformed.transformation.hypot/y y
                        :transformation.hypot/result-key-path (u/mvec dst)})))

(defn hypot-dynamic
  ([service x-key y-key dst]
   (u/integrate-params service :transformation/hypot 0
                       {:transformation.hypot/x-key (u/mvec x-key)
                        :transformation.hypot/y-key (u/mvec y-key)
                        :transformation.hypot/result-key-path (u/mvec dst)})))
