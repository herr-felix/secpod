(defproject secpod-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
    [org.apache.logging.log4j/log4j-api "2.13.1"]
    [org.apache.logging.log4j/log4j-core "2.13.1"]
    [org.apache.logging.log4j/log4j-slf4j-impl "2.13.1"]

    [org.apache.sshd/sshd-core "2.4.0"]
    ]
  :main secpod-server.core
  :skip-aot secpod-server.core
  :target-path "target/%s"
  :jvm-opts ["-Dclojure.tools.logging.factory=clojure.tools.logging.impl/slf4j-factory" "--illegal-access=deny"]
  :profiles {:uberjar {:aot :all}})
