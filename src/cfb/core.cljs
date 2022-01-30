(ns cfb.core
  (:require [cfb.assignments :as a]
            [cfb.views :as v]
            [reagent.core :as r]
            [reagent.dom :as d]
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
  (let [assignment (a/assignments index)]
    (clear!)
    (reset! description (:description assignment))
    (reset! expressions (:expressions assignment))
    (reset! solution (:solution assignment))))

(defn evaluate! [expressions]
  (clear!)
  (try
    (reset! answer (sci/eval-string expressions))
    (reset! show-result? true)
    (catch :default e (reset! error-message (.-message e)))))

(defn show-next-assignment! []
  (swap! index inc)
  (show-assignment! @index))

(defn show-previous-assignment! []
  (swap! index dec)
  (show-assignment! @index))

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
   [:button {:on-click show-previous-assignment!
             :disabled (<= @index 0)} "Previous assignment"]
   [:button {:on-click show-next-assignment!
             :disabled (>= (inc @index) number-of-assignments)} "Next assignment"]])

(defn mount-root []
  (d/render [app] (.getElementById js/document "app")))

(defn ^:export init! []
  (show-assignment! @index)
  (mount-root))
