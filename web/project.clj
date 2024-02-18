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

(defproject org.cloboss/web "2.1.11-SNAPSHOT"
  :description "Serve web requests using Ring handlers, Servlets, or Undertow HttpHandlers."
  :plugins [[lein-modules "0.3.11"]]
  :java-source-paths ["src"]

  :dependencies [;;[org.cloboss/core _]
                 [org.cloboss/core "2.1.11-SNAPSHOT"]
                 ;;[org.projectodd.atticboss/atticboss-web-undertow _]
                 [org.projectodd.atticboss/atticboss-web-undertow "0.13.2-SNAPSHOT"]
                 ;;[ring/ring-core _]
                 [ring/ring-core "1.11.0"]]

  :profiles {:dev
             {:dependencies
              ;; [[io.pedestal/pedestal.service _]
              ;;  [http.async.client _]
              ;;  [org.clojars.tcrawley/gniazdo _]
              ;;  [ring/ring-devel _]
              ;;  [compojure _]
              ;;  [clj-http _]
              ;;  [org.glassfish.jersey.media/jersey-media-sse _]]
              ;; TODO: update these dev dependencies
              [[io.pedestal/pedestal.service "0.7.0-SNAPSHOT"] ;; for jakarta
               [http.async.client "1.2.0"]
               [org.clojars.tcrawley/gniazdo "0.4.1b"]
               [ring/ring-devel "1.11.0"]
               [compojure "1.5.0"]
               [clj-http "3.12.3"]
               [org.glassfish.jersey.media/jersey-media-sse "2.15"]]}}

  :test-paths ["test" "test-integration"])
