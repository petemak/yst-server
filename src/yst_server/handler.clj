(ns yst-server.handler
  (:require [yst-server.db :as db]
            [clojure.pprint :as pprint]
            [ring.util.request :as req]
            [ring.util.response :as res]))



(defn- uuid
  "Generate unique id"
  []
  (-> (java.util.UUID/randomUUID)
      (.toString)
      (subs 0 9)))


(defn- user-id
  "Extracts remote address and prepends with a UUID"
  [req]
  (str (uuid) (:remote-addr req)))



(defn home
  [req]
  (-> (res/response "Survey Tool!")))


;;
;; survey questions
;;
(defn get-questions
  "Return survey questions"
  [req]
  (let [qs (db/get-questions)]
    (res/response qs)))


;; {:protocol "HTTP/1.1",
;;  :remote-addr "localhost",
;;  :headers
;;  {"host" "localhost",
;;   "content-type" "application/json",
;;   "content-length" "49"},
;;  :server-port 80,
;;  :content-length 49,
;;  :content-type "application/json",
;;  :uri "/answer",
;;  :server-name "localhost",
;;  :body
;;  #object[java.io.ByteArrayInputStream 0x4847140b
;;                                       "java.io.ByteArrayInputStream@4847140b"],
;;  :scheme :http,
;;  :request-method :post}
(defn register-answer
  "Record the answer submitted in the for parameters
  We will assume questions and answers in  a map format
  {:q1 :ax
   :q2 :ay
   :q3 :az}
  Returns the user id used to register the answer"
  [req]
  (let [uid (user-id req)]
    (println ":::::::::::::::::::::::::::::")
    (println (:body req))
    (doseq [[q a] (:body req)]
      (db/register-answer q a uid))
    (-> (res/response uid))))
