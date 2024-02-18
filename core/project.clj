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

(defproject org.cloboss/core "2.1.11-SNAPSHOT"
  :description "Utilities shared by Immutant libs."
  :plugins [[lein-modules "0.3.11"]]

  :dependencies [;;[org.clojure/java.classpath _]
                 [org.clojure/java.classpath "0.2.3"]
                 ;;[org.clojure/tools.reader _]
                 [org.clojure/tools.reader "1.3.7"]]

  :profiles {:dev

             {:dependencies [[cheshire "5.12.0"]
                             ;;[cheshire _]
                             ;;[org.clojure/data.fressian _]
                             [org.clojure/data.fressian "1.0.0"]]}})
