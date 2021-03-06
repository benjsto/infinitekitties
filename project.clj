(defproject infinitekitties "0.1.0-SNAPSHOT"
  :description "Infinite Kitties"
  :url "http://infinitekitties.com"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [lib-noir "0.9.5"]
                 [ring-server "0.3.1"]
                 [ring/ring-core "1.3.2"]
                 [selmer "0.7.7"]
                 [environ "1.0.0"]
                 [clj-http "1.0.1"]]
  :uberjar-name "infinitekitties.jar"
  :repl-options {:init-ns infinitekitties.repl}
  :jvm-opts ["-server"]
  :plugins [[lein-ring "0.9.0"]
            [lein-environ "1.0.0"]
            [lein-ancient "0.5.5"]]
  :ring {:handler infinitekitties.handler/app
         :init    infinitekitties.handler/init
         :destroy infinitekitties.handler/destroy
         :uberwar-name "infinitekitties.war"}
  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
             :aot :all}
   :production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.2"]
                        [pjstadig/humane-test-output "0.6.0"]]
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}}
  :min-lein-version "2.0.0")
