;; Copyright 2014-2017 Red Hat, Inc, and individual contributors.
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;; http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(defproject org.cloboss/cloboss-parent "2.2.0-SNAPSHOT"
  :description "Parent for all that is Cloboss"
  :plugins [[lein-modules "0.3.11"]]
  :packaging "pom"

  :profiles {:pedantic {:pedantic? :abort}
             :provided {:dependencies [[org.clojure/clojure _]]}
             :travis {:modules {:subprocess "lein2"}}
             :incremental {:deploy-repositories [["releases"
                                                  {:url "dav:https://repository-projectodd.forge.cloudbees.com/incremental"
                                                   :sign-releases false}]]
                           :plugins [[lein-webdav "0.1.0"]]}
             :dev {:dependencies [[pjstadig/humane-test-output "0.6.0"]]
                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]}
             :integs {}
             :clojure-1.8 {:modules {:versions {clojure "1.8.0"}}}
             :clojure-1.9 {:modules {:versions {clojure "1.9.0-alpha14"}}}}

  :aliases {"docs-from-index" ["build-helper" "docs" "generate" "docs/guides"
                               "caching" "core" "messaging" "scheduling" "transactions" "web" "wildfly"]
            "docs" ["do" "modules" "doc-index" "," "docs-from-index"]}
  :modules  {:subprocess nil
             :inherited {:dependencies [[top.atticboss/atticboss-clojure _]
                                        [org.clojure/clojure _]]
                         :aliases {"-i" ^:replace ["with-profile" "+integs"]
                                   "doc-index" ^:replace ["build-helper" "docs" "generate-index"]
                                   "all" ^:displace ["do" "clean," "check," "test," "install"]}
                         :scm {:dir "."}
                         :license {:name "Apache Software License - v 2.0"
                                   :url "http://www.apache.org/licenses/LICENSE-2.0"
                                   :distribution :repo}
                         :plugins [[org.cloboss/build-helper "0.2.10"]
                                   [lein-file-replace "0.1.0"]]
                         :hooks [build-helper.plugin.pom/hooks]

                         :signing {:gpg-key "BFC757F9"}
                         :deploy-repositories [["releases" {:url "https://clojars.org/repo/" :creds :gpg}]]}

             :versions {clojure                    "1.10.3"
                        java.classpath             "1.0.0"
                        tools.nrepl                "0.2.12"
                        tools.reader               "0.10.0"
                        ring                       "1.11.0"
                        clj-time                   "0.9.0"
                        cheshire                   "5.4.0"
                        data.fressian              "0.2.0"
                        core.memoize               "0.5.9"
                        io.pedestal/service        "0.7.0-SNAPSHOT" ;;for jakarta
                        http.async.client          "1.2.0"
                        gniazdo                    "0.4.1b"
                        compojure                  "1.5.0"
                        org.clojure/java.jdbc      "0.7.12"
                        h2                         "1.4.200"
                        jersey-media-sse           "2.15"
                        jersey-client              "2.15"
                        clj-http                   "3.12.3"
                        environ                    "1.0.3"

                        top.atticboss  "0.14.0-SNAPSHOT"
                        top.atticboss/atticboss-messaging-artemis "0.14.0-SNAPSHOT"
                        ;; top.atticboss  "1.x.incremental.321"
                        ;; top.atticboss  "0.14.0-SNAPSHOT"

                        org.cloboss               :version
                        fntest                     "2.0.8"}}
  :release-tasks  [["vcs" "assert-committed"]

                   ["change" "version" "leiningen.release/bump-version" "release"]
                   ["with-profile" "integs" "modules" "change" "version" "leiningen.release/bump-version" "release"]

                   ["modules" ":dirs" ".,web,messaging,transactions,scheduling,caching"
                    "file-replace" "README.md" "(<version>| \")" "(\"]|</version>)" "version"]

                   ["vcs" "commit"]
                   ["vcs" "tag"]
                   ["modules" "deploy"]

                   ["change" "version" "leiningen.release/bump-version"]
                   ["with-profile" "integs" "modules" "change" "version" "leiningen.release/bump-version"]

                   ["vcs" "commit"]
                   ["vcs" "push"]])
