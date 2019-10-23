(ns yst-server.routes-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as json]
            [ring.mock.request :as mock]
            [yst-server.routes :refer :all]))



(deftest routes-test
  (testing "home route"
    (let [req (mock/request :get "/")
          res (app req)]
      (is (= (:status res) 200))
      (is (= (:body res) "Survey Tool!"))))
  (testing "questions route"
    (let [req (mock/request :get "/questions")
          res (app req)
          body (json/decode (:body res))]
      (println "================== questons ==========================")
      (clojure.pprint/pprint (:body res))
      (println "::->")
      (clojure.pprint/pprint body)
      (is (= (:status res) 200))
      (is (= (some? body) true))
      (is (> (count body) 1))
      (is (= (get-in body ["q1" "id"]) "Question 1"))) )
  (testing "answers route"
    (let [req (-> (mock/request :put "/answers")
                  (mock/json-body {:q1 :a2 :q2 :a3 :q3 :a7}))
          res (app req)        
         ;; body (json/decode (:body res))
          ]
      (println "=================== answers =========================")
      (clojure.pprint/pprint (:body res))
      (is (= (:status res) 200))
      (is (= (some? (:body res)) true)))))
