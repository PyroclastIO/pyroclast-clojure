(defproject io.pyroclast/pyroclast-clojure "0.2.1"
  :description "A Clojure client for Pyroclast"
  :url "https://github.com/onyx-platform/pyroclast-clojure"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [cheshire "5.7.1"]
                 [clj-http "3.5.0"]
                 [funcool/promesa "1.9.0"]]
  :test-selectors {:topic :topic})
