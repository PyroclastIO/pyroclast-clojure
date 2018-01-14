(defproject io.pyroclast/pyroclast-clojure "0.2.1"
  :description "A Clojure client for Pyroclast"
  :url "https://github.com/onyx-platform/pyroclast-clojure"
  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.8.51" :scope "provided"]
                 [com.taoensso/timbre "4.10.0"]
                 [cheshire "5.7.1"]
                 ;;[clj-http "3.5.0"]
                 [io.nervous/kvlt "0.1.4"]
                 ;;[funcool/promesa "1.9.0"]
                 [funcool/promesa     "1.6.0"]]
  :plugins [[lein-cljsbuild "1.1.7"]
            ]
  :clean-targets ["release-js/pyroclast.bare.js"]
  :cljsbuild {:builds
              [
               {:id "release"
                :source-paths ["src"]
                :assert true
                :compiler {
                           :output-to "release-js/pyroclast.bare.js"
                           :optimizations :advanced
                           :npm-deps {"request" "2.72.0"
                                      "websocket" "1.0.22"
                                      "eventsource" "0.1.6"
                                      "source-map-support" "0.4.0"}
                           :install-deps true
                           :output-wrapper false            ;; Prevent wrapping functions, since we do it with wrap_bare.sh
                           :parallel-build true
                           :checked-arrays :warn
                           }
                :notify-command ["release-js/wrap_bare.sh"]}]}
  :test-selectors {:topic :topic})
