(ns cfb.core
  (:require [cfb.assignments :as a]
            [cfb.views :as v]
            [reagent.core :as r]
            [reagent.dom :as d]
            [reitit.frontend :as rf]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfe]
            [sci.core :as sci]))

(def number-of-assignments (count a/assignments))

(defonce index (r/atom 0))
(defonce description (r/atom (:description (a/assignments @index))))
(defonce expressions (r/atom (:expressions (a/assignments @index))))
(defonce solution (r/atom nil))

(defonce answer (r/atom nil))
(defonce show-result? (r/atom false))
(defonce error-message (r/atom nil))

(defn clear! []
  (reset! answer nil)
  (reset! show-result? false)
  (reset! error-message nil))

(defn show-assignment! [index]
  (if-let [assignment (get a/assignments index)]
    (do
      (clear!)
      (reset! description (:description assignment))
      (reset! expressions (:expressions assignment))
      (reset! solution (:solution assignment)))
    (rfe/push-state :home)))

(defn evaluate! [expressions]
  (clear!)
  (try
    (let [result (sci/eval-string expressions)
          evaluated-result (if (seq? result) (doall result) result)]
      (reset! answer evaluated-result)
      (reset! show-result? true))
    (catch :default e (reset! error-message (.-message e)))))

(defn show-next-assignment! []
  (rfe/push-state :assignment {:index (inc @index)}))

(defn show-previous-assignment! []
  (rfe/push-state :assignment {:index (dec @index)}))

(defn update-expressions! [new-expressions]
  (reset! expressions new-expressions))

(defn app []
  [:div.app
   [:h1 "Clojure for beginners"]
   [:div.description @description]
   [v/editor @expressions update-expressions! #(evaluate! @expressions)]
   [v/check-answer-button #(evaluate! @expressions)]
   [v/result @answer @solution @show-result?]
   [v/error @error-message]
   [:div
    [:button {:on-click show-previous-assignment!
              :disabled (<= @index 0)} "Previous assignment"]
    [:button {:on-click show-next-assignment!
              :disabled (>= (inc @index) number-of-assignments)} "Next assignment"]]])

(defn mount-root []
  (d/render [app] (.getElementById js/document "app")))

(def routes
  (rf/router
   [["/" {:name :home
          :controllers [{:start (fn [_] (reset! index 0))}]}]
    ["/assignment/:index" {:name :assignment
                           :controllers [{:identity
                                          (fn [match]
                                            (js/parseInt (get-in match [:parameters :path :index])))
                                          :start
                                          (fn [new-index]
                                            (reset! index new-index)
                                            (show-assignment! @index))}]}]]))

(defonce match (r/atom nil))

(defn init-routes! []
  (rfe/start!
   routes
   (fn [new-match]
     (swap! match (fn [old-match]
                    (if new-match
                      (assoc new-match :controllers (rfc/apply-controllers (:controllers old-match) new-match))
                      (rfe/push-state :home)))))
   {:use-fragment false}))

(defn ^:export init! []
  (init-routes!)
  (show-assignment! @index)
  (mount-root))
