(ns phonebook.components.migrations
  (:require [integrant.core :as ig]
            [migratus.core :as migratus]))

(defmethod ig/init-key ::migrations
  [_ {:keys [options db]}]
  (let [config (assoc-in options [:db :datasource] db)]
    (migratus/migrate config)))
