(ns phonebook.components.handler
  (:require [integrant.core :as ig]
            [muuntaja.core]
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
     ["/contacts" ::todo]]]])

(defmethod ig/init-key ::handler
  [_ _]
  (reitit.ring/ring-handler
   (reitit.ring/router
    (routes-v1)
    {:data
     {:muuntaja   muuntaja.core/instance
      :middleware [reitit.ring.middleware.parameters/parameters-middleware
                   reitit.ring.middleware.muuntaja/format-middleware
                   reitit.ring.middleware.exception/exception-middleware]}})
   (reitit.ring/create-default-handler)))
