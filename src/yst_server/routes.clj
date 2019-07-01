(ns yst-server.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [yst-server.handler :as handler]
            [yst-server.middleware :as middleware]))



(defroutes app-routes
  (GET "/" [] "Survey Service")
  (route/not-found "Not Found"))


