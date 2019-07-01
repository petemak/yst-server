(ns yst-server.application
  (:require [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [yst-server.routes :as routes]))


(def app
  (wrap-defaults routes/app-routes api-defaults))
