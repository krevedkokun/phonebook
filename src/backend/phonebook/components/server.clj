(ns phonebook.components.server
  (:require [integrant.core :as ig]
            [ring.adapter.jetty :as jetty])
  (:import (org.eclipse.jetty.server Server)))

(defmethod ig/init-key ::server
  [_ {:keys [options handler]}]
  (let [options (assoc options :join? false)]
    (jetty/run-jetty handler options)))

(defmethod ig/halt-key! ::server
  [_ ^Server srv]
  (.stop srv))
