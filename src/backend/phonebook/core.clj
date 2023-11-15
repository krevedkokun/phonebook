(ns phonebook.core
  (:gen-class)
  (:require [integrant.core :as ig]
            [phonebook.components.api :as api]
            [phonebook.components.db :as db]
            [phonebook.components.migrations :as migrations]
            [phonebook.components.server :as server]))

(def config
  {::server/server
   {:handler (ig/ref ::api/api)
    :options {:port 9876}}

   ::db/db
   {:dbspec {:dbtype   "postgresql"
             :username "postgres"
             :password "postgres"
             :dbname   "phonebook"}}

   ::migrations/migrations
   {:options {:store         :database
              :migration-dir "migrations/"}
    :db      (ig/ref ::db/db)}

   ::api/api
   {:migrations (ig/ref ::migrations/migrations)
    :db         (ig/ref ::db/db)}})

(defn -main
  [& _args]
  (ig/init config))

(comment
  (def system (ig/init config))

  (ig/halt! system))
