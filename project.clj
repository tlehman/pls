(defproject pls "0.2.0-SNAPSHOT"
  :description "A library for working with PLS files"
  :url "https://github.com/tlehman/pls"
  :license {:name "EPL-2"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clj-http "3.12.3"]]
  :repl-options {:init-ns pls.core})
