(defproject tracer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["bintray" "https://dl.bintray.com/crate/crate"]]    ; Repo for Crate JDBC driver
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.3.443"]
                 [org.clojure/tools.logging "0.3.1"]                  ; logging framework
                 [clojure-watch "0.1.11"]
                 [mvxcvi/puget "1.0.1"]
                 [clj-http "3.7.0"]
                 [cheshire "5.8.0"]
                 [potemkin "0.4.4"]
                
                 
                 ]
  :main tracer.core)
