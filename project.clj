(defproject io.pyroclast/pyroclast-clojure "0.1.6"
  :description "A Clojure client for Pyroclast"
  :url "https://github.com/onyx-platform/pyroclast-clojure"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [cheshire "5.7.1"]
                 [clj-http "3.5.0"]
                 [io.forward/yaml "1.0.6"]]
  :test-selectors {:roaming :roaming
                   :topic :topic})
