(ns cfb.core
  (:require ["@codemirror/legacy-modes/mode/clojure" :refer [clojure]]
            ["@codemirror/stream-parser" :refer [StreamLanguage]]
            ["@uiw/react-codemirror" :default CodeMirror]
            [reagent.core :as r]
            [reagent.dom :as d]
            [sci.core :as sci]))

(defonce assignments
  [{:description "Blaat"
    :expressions "(def x [1 2 3 4])"
    :solution 10}
   {:description "Aap"
    :expressions "(def x [1 2 3 4])"
    :solution 24}])
(def number-of-assignments (count assignments))

(defonce index (r/atom 0))
(defonce description (r/atom (:description (assignments @index))))
(defonce expressions (r/atom (:expressions (assignments @index))))
(defonce solution (r/atom nil))

(defonce answer (r/atom nil))
(defonce show-result? (r/atom false))
(defonce error-message (r/atom nil))

(defn show-assignment! [index]
  (let [assignment (assignments index)]
    (reset! description (:description assignment))
    (reset! expressions (:expressions assignment))
    (reset! solution (:solution assignment))))

(show-assignment! @index)

(defn editor [expressions on-change]
  [(r/adapt-react-class CodeMirror) {:extensions [(.define StreamLanguage clojure)]
                                     :height "10rem"
                                     :on-change (fn [value] (on-change value))
                                     :theme "dark"
                                     :value expressions
                                     :width "40rem"}])

(defn result [answer solution show-result?]
  (js/console.log answer solution)
  (when show-result?
    [:div
     [:span (if (= answer solution) "YES!" "NO!")]
     [:pre [:code (pr-str answer)]]]))

(defn error [error-message]
  [:div error-message])

(defn evaluate! [expressions]
  (try
    (reset! answer (sci/eval-string expressions))
    (reset! show-result? true)
    (catch :default e (reset! error-message (.-message e)))))

(defn next-assignment! []
  (swap! index inc)
  (show-assignment! @index))

(defn previous-assignment! []
  (swap! index dec)
  (show-assignment! @index))

(defn app []
  [:div.app
   [:div @description]
   [editor @expressions (fn [new-expressions] (reset! expressions new-expressions))]
   [:button {:on-click #(evaluate! @expressions)} "Check answer"]
   [result @answer @solution @show-result?]
   [error @error-message]
   [:button {:on-click previous-assignment!
             :disabled (<= @index 0)} "Previous question"]
   [:button {:on-click next-assignment!
             :disabled (>= (inc @index) number-of-assignments)} "Next question"]])

(defn mount-root []
  (d/render [app] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
