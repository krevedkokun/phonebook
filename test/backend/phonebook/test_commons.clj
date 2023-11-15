(ns phonebook.test-commons
  (:require [integrant.core :as ig]
            [phonebook.components.db :as db]
            [phonebook.components.migrations :as migrations]))

(def test-config
  {::db/db
   {:dbspec {:dbtype   "postgresql"
             :username "postgres"
             :password "postgres"
             :dbname   "phonebook_test"}}

   ::migrations/migrations
   {:options {:store         :database
              :migration-dir "migrations/"}
    :db      (ig/ref ::db/db)}})

(defonce test-system (atom nil))

(defn stop-test-system
  []
  (when-let [sys @test-system]
    (ig/halt! sys)
    (reset! test-system nil)))

(defn start-test-system
  []
  (stop-test-system)
  (reset! test-system (ig/init test-config)))

(defn get-test-db
  []
  (:phonebook.components.db/db @test-system))
