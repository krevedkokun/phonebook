(ns phonebook.core
  (:gen-class)
  (:require [integrant.core :as ig]
            [phonebook.components.db :as db]
            [phonebook.components.handler :as handler]
            [phonebook.components.server :as server]))

(def config
  {::server/server   {:handler (ig/ref ::handler/handler)
                      :options {:port 9876}}
   ::db/db           {:options {:dbtype   "postgresql"
                                :username "postgres"
                                :password "postgres"
                                :dbname   "phonebook"}}
   ::handler/handler {:db (ig/ref ::db/db)}})

(defn -main
  [& _args]
  (ig/init config))

(comment
  (def system (ig/init config))

  (ig/halt! system))
