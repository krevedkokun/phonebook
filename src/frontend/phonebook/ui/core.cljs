(ns phonebook.ui.core
  (:require
   [day8.re-frame.http-fx]
   [phonebook.ui.pages.contact]
   [phonebook.ui.pages.form]
   [phonebook.ui.pages.grid]
   [re-frame.core :as rf]
   [reagent.dom :as rdom]
   [reitit.frontend]
   [reitit.frontend.controllers]
   [reitit.frontend.easy]))

(def routes
  (reitit.frontend/router
   ["/"
    [""
     {:name        :grid
      :view        phonebook.ui.pages.grid/page
      :controllers [phonebook.ui.pages.grid/controller]}]
    ["new"
     {:name        :form
      :view        phonebook.ui.pages.form/page
      :controllers [phonebook.ui.pages.form/controller]}]
    [":name"
     {:name        :contact
      :view        phonebook.ui.pages.contact/page
      :controllers [phonebook.ui.pages.contact/controller]}]]
   {:conflicts nil}))

(rf/reg-fx
 :push-state
 (fn [route]
   (apply reitit.frontend.easy/push-state route)))

(rf/reg-fx
 :set-query
 (fn [params]
   (apply reitit.frontend.easy/set-query params)))

(rf/reg-sub
 :current-route
 (fn [db]
   (:current-route db)))

(rf/reg-event-fx
 :navigated
 (fn [{:keys [db]} [_ new]]
   (let [old (:current-route db)
         controllers (-> (:controllers old)
                         (reitit.frontend.controllers/apply-controllers new))
         new (assoc new :controllers controllers)]
     {:db (assoc db :current-route new)})))

(defn current-page
  []
  (if-let [{{:keys [view]} :data} @(rf/subscribe [:current-route])]
    [view]
    [:div "hello"]))

(defn on-navigate
  [new]
  (when new
    (rf/dispatch [:navigated new])))

(defn init-routes!
  []
  (reitit.frontend.easy/start! routes on-navigate {:use-fragment true}))

(defn ^:dev/after-load start
  []
  (rf/clear-subscription-cache!)
  (init-routes!)
  (rdom/render [current-page]
               (.getElementById js/document "app-content")))

(defn ^:export main
  []
  (start))
