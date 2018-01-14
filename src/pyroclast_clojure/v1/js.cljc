(ns pyroclast-clojure.v1.js)


(defn ^:export hello_world []
  #?(:cljs (js/console.log "Hello world!")))