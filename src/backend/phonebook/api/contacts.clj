(ns phonebook.api.contacts)

(defn create-contact
  [{:keys [body-params db]}]
  {:status 201})

(defn search-contacts
  [{:keys [query-params db]}]
  {:status 200})

(defn read-contact
  [{:keys [path-params db]}]
  {:status 200})

(defn delete-contact
  [{:keys [path-params db]}]
  {:status 204})
