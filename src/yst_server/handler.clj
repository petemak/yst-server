(ns yst-server.handler
  (:require [yst-server.db :as db]
            [ring.util.request :as req]
            [ring.util.response :as res]))


(defn home
  [req]
  (-> (res/response "Invalid request!")
      (res/status 404)
      (res/content-type "text/html")
      (res/charset "utf-8")))


(defn add-answer
  [stg user qid aid]
  (if-let [answer (db/add-answer stg qid aid)]
    (res/response aid)
    (res/response (res/status 404))))
