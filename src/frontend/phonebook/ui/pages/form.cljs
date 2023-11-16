(ns phonebook.ui.pages.form
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]
            [reitit.frontend.easy :refer [href]]))

(rf/reg-event-fx
 ::deinit
 (fn [{:keys [db]} _]
   {:db (dissoc db ::db)}))

(def controller
  {:stop (fn [_] (rf/dispatch [::deinit]))})

(rf/reg-event-fx
 ::create-success
 (fn [{:keys [db]} [_ {:contacts/keys [name]}]]
   (prn name)
   {:db (assoc-in db [::db :create :loading?] false)
    :push-state [:contact {:name name}]}))

(rf/reg-event-fx
 ::create-failure
 (fn [{:keys [db]} _]
   {:db (assoc-in db [::db :create :loading?] false)}))

(rf/reg-event-fx
 ::create-contact
 (fn [{:keys [db]} [_ val]]
   {:db (assoc-in db [::db :create :loading?] true)
    :http-xhrio
    {:method          :post
     :uri             "http://localhost:9876/api/v1/contacts"
     :params          {:name  (.. val -target -name -value)
                       :phone (.. val -target -phone -value)}
     :format          (ajax/json-request-format)
     :response-format (ajax/json-response-format {:keywords? true})
     :on-success      [::create-success]
     :on-failure      [::create-failure]}}))

(rf/reg-sub
 ::creating?
 (fn [db]
   (get-in db [::db :create :loading?])))

(defn page
  []
  (let [creating? @(rf/subscribe [::creating?])]
    [:form {:on-submit #(do
                          (.preventDefault %)
                          (rf/dispatch [::create-contact %]))}
     [:article
      [:nav {:aria-label "breadcrumb"}
       [:ul
        [:li [:a {:href (href :grid)} "Home"]]
        [:li "New Contact"]]]
      [:footer
       [:label {:for "name"}
        "Name"
        [:input#name {:name "name"
                      :type "text"}]]
       [:label {:for "phone"}
        "Phone"
        [:input#phone {:name "phone"
                       :type "text"}]]
       [:button {:disabled creating?
                 :type "submit"}
        "Submit"]]]]))
