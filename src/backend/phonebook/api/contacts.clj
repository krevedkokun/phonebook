(ns phonebook.api.contacts
  (:require [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]))

(defn create-contact
  [{:keys [body-params]
    {{:keys [db]} :data} :reitit.core/match}]
  (if-let [contact (sql/get-by-id db :contacts (:name body-params) :name {})]
    {:status 200
     :body contact}
    (let [contact (sql/insert! db :contacts body-params)]
      {:status 201
       :body contact})))

(defn search-contacts
  [{:keys [query-params]
    {{:keys [db]} :data} :reitit.core/match}]
  (let [pattern (str "%" (get query-params "name") "%")
        contacts (->> ["select * from contacts where name ilike ?" pattern]
                      (sql/query db))]
    {:status 200
     :body contacts}))

(defn read-contact
  [{:keys [path-params]
    {{:keys [db]} :data} :reitit.core/match}]
  (if-let [contact (sql/get-by-id db :contacts (:name path-params) :name {})]
    {:status 200
     :body contact}
    {:status 404}))

(defn delete-contact
  [{:keys [path-params]
    {{:keys [db]} :data} :reitit.core/match}]
  (if (sql/delete! db :contacts
                   {:name (:name path-params)}
                   {:suffix "returning *"})
    {:status 204}
    {:status 404}))
