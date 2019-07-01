(ns yst-server.application-test
  (:require [yst-server.application :as appl]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]))



(deftest test-app
  (testing "main route"
    (let [response (appl/app (mock/request :get "/"))]
      (testing "that status code is 200"
        (is (= (:status response) 200)))
      (testing "that response message is as expected"       
        (is (= (:body response) "Survey Service")))))

  (testing "not-found route"
    (let [response (appl/app (mock/request :get "/invalid"))]
      (testing "that response status code is 404"
        (is (= (:status response) 404))))))
