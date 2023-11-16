(ns phonebook.api.static
  (:require [hiccup.page]))

(defn index
  [_req]
  {:status 200
   :body
   (hiccup.page/html5
    [:head
     [:meta {:charset "UTF-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1.0"}]
     [:title "phonebook"]]
    (hiccup.page/include-css "https://cdn.jsdelivr.net/npm/@picocss/pico@1/css/pico.min.css")
    [:body
     (hiccup.page/include-js "js/main.js")
     [:main#app-content.container-fluid]])})
