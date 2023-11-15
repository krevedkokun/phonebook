(ns phonebook.core
  (:gen-class)
  (:require [integrant.core :as ig]
            [phonebook.components.handler :as handler]
            [phonebook.components.server :as server]))

(def config
  {::server/server   {:handler (ig/ref ::handler/handler)
                      :options {:port 9876}}
   ::handler/handler {}})

(defn -main
  [& _args]
  (ig/init config))

(comment
  (def system (ig/init config))

  (ig/halt! system))
