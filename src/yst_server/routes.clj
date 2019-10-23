(ns yst-server.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [yst-server.handler :as handler]
            [yst-server.middleware :as middleware]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))




;;
;; Serves 3 routs
;; GET "/" -> handler/home
;; GET "/questions" -> handler/questions
;; PUT "/answer" -> handler/register-answer
;; GET "/results" -> handler/results
(defroutes app-routes
  (GET "/" [:as req] (handler/home req))
  (GET "/questions" [:as req] (handler/get-questions req))
  (PUT "/answers" [:as req] (handler/register-answer req)) 
  (route/not-found "Not Found") )



;; Main handler
;; replaces:   (wrap-defaults (mw/wrap-slurp-boyx routes/app-routes) api-defaults)
(def app
  (-> app-routes
      (wrap-defaults api-defaults)
      (wrap-json-body)
      ;;(middleware/wrap-slurp-body)
      (wrap-json-response)))
