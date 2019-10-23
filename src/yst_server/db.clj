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
;; Question storge as in-memory database
;;====================================================================
(def qst-stg (atom {}))

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
;; Read questions template and intialise databse
;;====================================================================
(defn- init-qst-stg
  "Load questions from file to intialise atom"
  []
  (reset! qst-stg (read-string (slurp "question-template.edn"))))

;;====================================================================
;; Read questions template and intialise databse
;;====================================================================
(defn- init-ans-stg
  "Load answer template from file to intialise atom"
  []
  (reset! ans-stg (read-string (slurp "answer-template.edn"))))


;;====================================================================
;; Intialise storage
;;====================================================================
(defn init-stg
  []
  (when (empty? @qst-stg)
    (init-qst-stg))
  (when (empty? @ans-stg)
    (init-ans-stg)))

;;====================================================================
;; Compoent: response database
;;====================================================================
(defstate resp-db :start (init-stg)
                  :stop (save-db))



;;====================================================================
;; Question database
;;====================================================================

(defn get-questions
  "Get questions for the survey"
  []
  (when (empty? @qst-stg)
    (init-qst-stg))
  (:questions @qst-stg))


;;====================================================================
;; User and response registrations
;;====================================================================

(defn register-user
  "Register answer to quesion did of survey sid for user sid"
  [uid name loc]
  (swap! ans-stg assoc-in [:users uid :name] name)
  (swap! ans-stg assoc-in [:users uid :location] loc))


(defn register-answer
  "Register the answer a user uid"
  [qid aid uid]
  (when (empty? @ans-stg)
    (init-ans-stg))
  (swap! ans-stg update-in [:questions qid :answers aid] (fnil conj []) uid))


;;====================================================================
;; Data analsyis
;;====================================================================
(defn- count-elements
  [acc elm]
  (assoc acc (first elm) (count (second elm))))


(defn count-votes
  "Count number of votes for the  question"
  [qid]
  (let [acc {}
        answers (get-in @ans-stg [:questions qid :answers])]
    (reduce count-elements acc answers)))
