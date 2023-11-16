(ns phonebook.ui.pages.grid
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]
            [reitit.frontend.easy :refer [href]]))

(rf/reg-event-fx
 ::init-success
 (fn [{:keys [db]} [_ res]]
   {:db (-> db
            (assoc-in [::db :contacts :loading?] false)
            (assoc-in [::db :contacts :data] res))}))

(rf/reg-event-fx
 ::init-failure
 (fn [{:keys [db]} _]
   {:db (assoc-in db [::db :contacts :loading?] false)}))

(rf/reg-event-fx
 ::init
 (fn [{:keys [db]} [_ {:keys [query]}]]
   {:db (assoc-in db [::db :contacts :loading?] true)
    :http-xhrio
    {:method :get
     :uri "http://localhost:9876/api/v1/contacts"
     :params query
     :response-format (ajax/json-response-format {:keywords? true})
     :on-success [::init-success]
     :on-failure [::init-failure]}}))

(rf/reg-event-fx
 ::search
 (fn [_ [_ search-val]]
   (prn search-val)
   {:set-query [{:name search-val}]}))

(rf/reg-event-fx
 ::deinit
 (fn [{:keys [db]} _]
   {:db (dissoc db ::db)}))

(rf/reg-sub
 ::contacts
 (fn [db]
   (get-in db [::db :contacts :data])))

(def controller
  {:parameters {:query [:name]}
   :start (fn [params] (rf/dispatch [::init params]))
   :stop (fn [_] (rf/dispatch [::deinit]))})

(defn page
  []
  (let [contacts @(rf/subscribe [::contacts])]
    [:article
     [:nav
      [:ul [:li "Home"]]
      [:ul
       [:li [:a {:href (href :form)
                 :role "button"} "New"]]]]
     [:footer
      [:input {:type "search"
               :on-change #(rf/dispatch [::search (.. % -target -value)])}]
      [:table {:role "grid"}
       [:thead
        [:tr
         [:th "Name"]
         [:th "Phone"]]]
       [:tbody
        (for [{:contacts/keys [name phone]} contacts]
          [:tr {:key name}
           [:td [:a {:href (href :contact {:name name})} name]]
           [:td phone]])]]]]))
