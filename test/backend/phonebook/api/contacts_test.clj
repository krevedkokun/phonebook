(ns phonebook.api.contacts-test
  (:require [clojure.test :refer [deftest is use-fixtures]]
            [next.jdbc :as jdbc]
            [phonebook.api.contacts :as sut]
            [phonebook.test-commons :as tc]))

(use-fixtures :once
  (fn [t]
    (tc/start-test-system)
    (t)
    (tc/stop-test-system)))

(use-fixtures :each
  (fn [t]
    (t)
    (jdbc/execute! (tc/get-test-db) ["delete from contacts"])))

(deftest create-contact-test
  (is (= {:status 201
          :body {:name "John Doe"
                 :phone "88005553535"}}
         (sut/create-contact
          {:db (tc/get-test-db)
           :body-params
           {:name "John Doe"
            :phone "88005553535"}})))

  (is (= {:status 200
          :body {:name "John Doe"
                 :phone "88005553535"}}
         (sut/create-contact
          {:db (tc/get-test-db)
           :body-params
           {:name "John Doe"
            :phone "53535550088"}}))))

(deftest search-contacts-test
  (doseq [r [{:name "John Doe"
              :phone "88005553535"}
             {:name "Jane Doe"
              :phone "88005553535"}
             {:name "Scarlet Johansson"
              :phone "88005553535"}
             {:name "Rich Hickey"
              :phone "88005553535"}]]
    (sut/create-contact
     {:db (tc/get-test-db)
      :body-params r}))

  (is (= {:status 200
          :body [{:name "John Doe"
                  :phone "88005553535"}
                 {:name "Scarlet Johansson"
                  :phone "88005553535"}]}
         (sut/search-contacts {:db (tc/get-test-db)
                               :query-params {:name "Joh"}})))

  (is (= {:status 200
          :body [{:name "John Doe"
                  :phone "88005553535"}
                 {:name "Scarlet Johansson"
                  :phone "88005553535"}]}
         (sut/search-contacts {:db (tc/get-test-db)
                               :query-params {:name "jOh"}})))
  (is (= {:status 200
          :body [{:name "John Doe"
                  :phone "88005553535"}
                 {:name "Scarlet Johansson"
                  :phone "88005553535"}]}
         (sut/search-contacts {:db (tc/get-test-db)
                               :query-params {:name "JOH"}})))
  (is (= {:status 200
          :body [{:name "John Doe"
                  :phone "88005553535"}
                 {:name "Scarlet Johansson"
                  :phone "88005553535"}]}
         (sut/search-contacts {:db (tc/get-test-db)
                               :query-params {:name "joh"}}))))

(deftest read-contact-test
  (sut/create-contact
   {:db (tc/get-test-db)
    :body-params {:name "John Doe"
                  :phone "88005553535"}})

  (is (= {:status 200
          :body {:name "John Doe"
                 :phone "88005553535"}}
         (sut/read-contact {:db (tc/get-test-db)
                            :path-params {:name "John Doe"}})))

  (is (= {:status 404}
         (sut/read-contact {:db (tc/get-test-db)
                            :path-params {:name "JOHN DOE"}}))))

(deftest delete-contact-test
  (sut/create-contact
   {:db (tc/get-test-db)
    :body-params {:name "John Doe"
                  :phone "88005553535"}})

  (is (= {:status 404}
         (sut/delete-contact {:db (tc/get-test-db)
                              :path-params {:name "JOHN DOE"}})))

  (is (= {:status 204}
         (sut/delete-contact {:db (tc/get-test-db)
                              :path-params {:name "John Doe"}})))

  (is (= {:status 404}
         (sut/delete-contact {:db (tc/get-test-db)
                              :path-params {:name "John Doe"}}))))
