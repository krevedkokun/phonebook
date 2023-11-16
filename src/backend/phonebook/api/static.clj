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
    (hiccup.page/include-css "https://unpkg.com/@picocss/pico@latest/css/pico.min.css")
    [:body
     (hiccup.page/include-js "js/main.js")
     #_[:script "window.onload = function() { chinese_dictionary.core.main(); }"]
     [:main#app-content.container-fluid {:style "padding-top: 0"}]])})
