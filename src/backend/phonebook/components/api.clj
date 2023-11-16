(ns phonebook.components.api
  (:require [integrant.core :as ig]
            [muuntaja.core]
            [phonebook.api.contacts :as contacts-api]
            [reitit.ring]
            [reitit.ring.coercion]
            [reitit.ring.middleware.exception]
            [reitit.ring.middleware.muuntaja]
            [reitit.ring.middleware.parameters]))

(defn routes-v1
  []
  [["/index.html" ::index]
   ["/api"
    ["/v1"
     ["/contacts"
      [""
       {:get  contacts-api/search-contacts
        :post contacts-api/create-contact}]
      ["/:name"
       {:get    contacts-api/read-contact
        :delete contacts-api/delete-contact}]]]]])

(defmethod ig/init-key ::api
  [_ {:keys [db]}]
  (reitit.ring/ring-handler
   (reitit.ring/router
    (routes-v1)
    {:data
     {:db         db
      :muuntaja   muuntaja.core/instance
      :middleware [reitit.ring.middleware.parameters/parameters-middleware
                   reitit.ring.middleware.muuntaja/format-middleware
                   reitit.ring.middleware.exception/exception-middleware]}})
   (reitit.ring/create-default-handler)))
