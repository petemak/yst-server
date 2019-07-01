(ns yst-server.db
  (:require [mount.core :refer [defstate]]))




;;====================================================================
;; Survey questions
;;====================================================================
(def survey
  {:name "Programmer Survey 2019"
   :users {"s1i5fs" {:name "Peter"
                     :location "Zurich"}}
   :questions {:q1 {:id "Quesion 1"
                     :text "How would you describe your role?"
                     :answers {:a1 "Developer/Programmer"
                               :a2 "Architect"
                               :a3 "Business Analyst"
                               :a4 "Project Manager"}}
                   
               :q2 {:id "Question 2"
                    :text "Which of these is your primary programming language?"
                    :answers {:a1 "JavaScript"
                              :a2 "Java"
                              :a3 "Scala"
                              :a4 "Python"
                              :a5 "Rust"
                              :a6 "Go"
                              :a7 "Clojure/ClojureScript"}}}})


;;====================================================================
;; Response template to survey questions
;;====================================================================
(def responses
  {:users {"s1i5fs" {:name "Peter"
                     :location "Zurich"}}
   :questions  {:q1 {:id "Question 1"
                     :text "Are you a programmer?"
                     :answers {:a1  [{:uid "s1i5fs" :location "Zurich"}] 
                               :a2  [{:uid "werwr"  :location "Bratislava"}]}}
                :q2 {:id "Question 2"
                     :text "Which of these is your primary programming language?"
                     :answers  {}}}})




;;====================================================================
;; Response database as in-memory database
;;====================================================================
(def ans-stg (atom {}))

;;====================================================================
;; Save to file
;;====================================================================
(defn save-db
  "Save in memory database to file"
  []
  (spit "ps-2019-results.edn" (prn-str @ans-stg)))


;;====================================================================
;; Read answer template and intialise databse
;;====================================================================
(defn init-db
  "Load DB from file to intialise atom"
  []
  (reset! ans-stg (read-string (slurp "ps-2019-result-template.edn"))))


;;====================================================================
;; Compoent: response database
;;====================================================================
(defstate resp-db :start (init-db)
                  :stop (save-db))



;;====================================================================
;; Question database
;;====================================================================

(defn get-questions
  "Get questions for the survey"
  []
  (:questions survey))


;;====================================================================
;; User and response registrations
;;====================================================================

(defn register-user
  "Register answer to quesion did of survey sid for user sid"
  [stg uid name loc]
  (swap! stg assoc-in [:users uid :name] name)
  (swap! stg assoc-in [:users uid :location] loc))


(defn register-answer
  "Register the answer a user chose"
  [stg qid aid uid]
  (swap! stg update-in [:questions qid :answers aid] (fnil conj []) uid))


;;====================================================================
;; Data analsyis
;;====================================================================
(defn count-elements
  [acc elm]
  (assoc acc (first elm) (count (second elm))))


(defn count-votes
  "Count number of votes for the  question"
  [stg qid]
  (let [acc {}
        answers (get-in @stg [:questions qid :answers])]
    (reduce count-elements acc answers)))
