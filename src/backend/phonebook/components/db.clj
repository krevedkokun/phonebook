(ns phonebook.components.db
  (:require [integrant.core :as ig]
            [next.jdbc :as jdbc]
            [next.jdbc.connection :as connection])
  (:import (com.zaxxer.hikari HikariDataSource)))

(defmethod ig/init-key ::db
  [_ {:keys [dbspec]}]
  (let [ds (connection/->pool HikariDataSource dbspec)]
    (.close (jdbc/get-connection ds))
    ds))

(defmethod ig/halt-key! ::db
  [_ ^HikariDataSource ds]
  (.close ds))
