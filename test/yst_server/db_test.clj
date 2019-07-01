(ns yst-server.db-test
  (:require [yst-server.db :refer :all]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]))


(defn db-test-fixture
  [f]
  (init-db)
  (f))

(use-fixtures :once db-test-fixture)

(deftest test-db
  (testing "Survey persitence"
    (testing "that answer template is loaded"
      (is (= false (empty? @ans-stg)))
      (is (= (count (get-in @ans-stg [:questions])) 2)))
    (testing "adding users"
      (let [uid "s1234"
            name "Adam"
            location "Cape Town" ]        
        (register-user ans-stg uid name location) 
        (is (= (get-in @ans-stg [:users uid :name]) name))))))
