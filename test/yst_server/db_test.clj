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
        (is (= (get-in @ans-stg [:users uid :name]) name))))
    (testing "saving an answer"
      (let [uid1 "s1231"
            uid2 "s2232"
            uid3 "s3233"
            uid4 "s4234"
            uid5 "s5235"
            uid6 "s6236"
            uid7 "s7237"
            uid8 "s8238"
            uid9 "s9239"
            uid10 "s1030"
            uid11 "s1131"
            uid12 "s1232"
            uid13 "s1313"]
        (register-answer ans-stg :q2 :a7 uid1)
        (register-answer ans-stg :q2 :a7 uid2)
        (register-answer ans-stg :q2 :a7 uid3)        
        (register-answer ans-stg :q2 :a6 uid4)
        (register-answer ans-stg :q2 :a5 uid5)
        (register-answer ans-stg :q2 :a5 uid6)        
        (register-answer ans-stg :q2 :a3 uid7)
        (register-answer ans-stg :q2 :a3 uid8)
        (register-answer ans-stg :q2 :a3 uid9)        
        (register-answer ans-stg :q2 :a3 uid10)
        (register-answer ans-stg :q2 :a2 uid11)
        (register-answer ans-stg :q2 :a1 uid12)        
        (register-answer ans-stg :q2 :a1 uid13)

        (is (= (count (get-in @ans-stg [:questions :q2 :answers :a7])) 3))
        (is (= (get-in @ans-stg [:questions :q2 :answers :a7]) [uid1 uid2 uid3]))
        (is (= (get-in @ans-stg [:questions :q2 :answers :a6]) [uid4]))
        (is (= (get-in @ans-stg [:questions :q2 :answers :a5]) [uid5 uid6]))
        (is (= (get-in @ans-stg [:questions :q2 :answers :a3]) [uid7 uid8 uid9 uid10]))
        (is (= (get-in @ans-stg [:questions :q2 :answers :a1]) [uid12 uid13]))))))
