(defproject net.crystae/blink1 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [local/blink1 "0.0.1-SNAPSHOT"]]
  :main ^:skip-aot blink1.core
  :target-path "target/%s"
  :repositories {"project" "file:repo"}
  :profiles {:uberjar {:aot :all}})
