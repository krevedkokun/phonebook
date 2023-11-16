(ns phonebook.ui.core
  (:require
   [day8.re-frame.http-fx]
   [phonebook.ui.pages.grid]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [reagent.dom :as rdom]
   [reitit.frontend]
   [reitit.frontend.controllers]
   [reitit.frontend.easy]))

(def routes
  (reitit.frontend/router
   ["/"
    ["" {:view phonebook.ui.pages.grid/page
         :controllers [{:start (fn [& _] (prn "start"))
                        :stop (fn [& _] (prn "stop"))}]}]]))

(rf/reg-fx
 :push-state
 (fn [route]
   (apply reitit.frontend.easy/push-state route)))

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
     (assoc db :current-route new))))

(defn current-page
  []
  (when-let [{{:keys [view]} :data} (rf/subscribe [:current-route])]
    [view]))

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
