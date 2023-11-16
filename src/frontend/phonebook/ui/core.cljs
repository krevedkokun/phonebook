(ns phonebook.ui.core
  (:require
   [day8.re-frame.http-fx]
   [re-frame.core :as rf]
   [reagent.dom :as rdom]))

(defn ^:dev/after-load start
  []
  (rf/clear-subscription-cache!)
  (rdom/render [:div "hello"]
               (.getElementById js/document "app-content")))

(defn ^:export main
  []
  (start))
