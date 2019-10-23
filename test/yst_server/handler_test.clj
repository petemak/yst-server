(ns yst-server.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [yst-server.handler :refer :all]))




(deftest test-hander
  (testing "home request"
    (let [req (mock/request :get "/")
          res (home req)]
      (is (= (:status res) 404))
      (is (= (with-out-str (clojure.pprint/pprint req)) (:body res)))))


  (testing "loading questions"
    (let [req (mock/request :get "/guestions")
          res (get-questions req)]
      (testing "that question list is not empty"
        (is (not (empty? (:body res))))
        (is (> (count (:body res)) 1)))))



  (testing "that saving of surbeys answers"
    (let [req (mock/request :put "/answer" {:q1 :a3 :q2 :a2 :q3 :a7})
          res (register-answer req)]
      (is (some? (:body res)))
      (is (= (:status res) 200)))))
