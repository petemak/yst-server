(ns yst-server.middleware)


(defn wrap-slurp-body
  "Returns a handler that reads/slurps the body if it 
  contains instance of the mutable input stream and updates the :body
  keyword with slurped string"
  [handler]
  (fn [request]
    (if (instance? java.io.InputStream (:body request))
      (let [slurped-request (update request :body slurp)]
        (handler slurped-request))
      (handler request))))



