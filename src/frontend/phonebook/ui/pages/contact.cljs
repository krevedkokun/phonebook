(ns phonebook.ui.pages.contact
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]
            [reitit.frontend.easy :refer [href]]))

(rf/reg-event-fx
 ::init-success
 (fn [{:keys [db]} [_ res]]
   {:db (-> db
            (assoc-in [::db :contact :loading?] false)
            (assoc-in [::db :contact :data] res))}))

(rf/reg-event-fx
 ::init-failure
 (fn [{:keys [db]} _]
   {:db (assoc-in db [::db :contact :loading?] false)}))

(rf/reg-event-fx
 ::init
 (fn [{:keys [db]} [_ {:keys [path]}]]
   {:db (assoc-in db [::db :contact :loading?] true)
    :http-xhrio
    {:method :get
     :uri (str "http://localhost:9876/api/v1/contacts/" (:name path))
     :response-format (ajax/json-response-format {:keywords? true})
     :on-success [::init-success]
     :on-failure [::init-failure]}}))

(rf/reg-event-fx
 ::delete-success
 (fn [{:keys [db]} _]
   {:db (assoc-in db [::db :create :deleting?] false)
    :push-state [:grid]}))

(rf/reg-event-fx
 ::delete-failure
 (fn [{:keys [db]} _]
   {:db (assoc-in db [::db :contact :deleting?] false)}))

(rf/reg-event-fx
 ::delete-contact
 (fn [{:keys [db]} [_ name]]
   {:db (assoc-in db [::db :contact :loading?] true)
    :http-xhrio
    {:method          :delete
     :uri             (str "http://localhost:9876/api/v1/contacts/" name)
     :format          (ajax/json-request-format)
     :response-format (ajax/json-response-format {:keywords? true})
     :on-success      [::delete-success]
     :on-failure      [::delete-failure]}}))

(rf/reg-sub
 ::loading?
 (fn [db]
   (get-in db [::db :contact :loading?])))

(rf/reg-sub
 ::contact
 (fn [db]
   (get-in db [::db :contact :data])))

(rf/reg-event-fx
 ::deinit
 (fn [{:keys [db]} _]
   {:db (dissoc db ::db)}))

(def controller
  {:parameters {:path [:name]}
   :start (fn [params] (rf/dispatch [::init params]))
   :stop (fn [_] (rf/dispatch [::deinit]))})

(defn page
  []
  (let [loading? @(rf/subscribe [::loading?])
        {:contacts/keys [name phone]} @(rf/subscribe [::contact])]
    [:article {:aria-busy loading?}
     (when-not loading?
       [:<>
        [:nav {:aria-label "breadcrumb"}
         [:ul
          [:li [:a {:href (href :grid)} "Home"]]
          [:li name]]]
        [:footer
         [:p phone]
         [:button {:on-click #(rf/dispatch [::delete-contact name])}
          "Delete"]]])]))
