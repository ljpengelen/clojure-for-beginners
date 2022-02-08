(ns cfb.views
    (:require ["@codemirror/legacy-modes/mode/clojure" :refer [clojure]]
              ["@codemirror/stream-parser" :refer [StreamLanguage]]
              ["@codemirror/view" :refer [EditorView keymap]]
              ["@uiw/react-codemirror" :default CodeMirror]
              [clojure.string :as str]
              [reagent.core :as r]))

(defn editor [expressions on-change on-evaluate]
  [(r/adapt-react-class CodeMirror) {:extensions [(.define StreamLanguage clojure)
                                                  (.of keymap #js [#js {"key" "mod-e"
                                                                        "run" on-evaluate}])
                                                  (.-lineWrapping EditorView)]
                                     :height "10rem"
                                     :on-change (fn [value] (on-change value))
                                     :theme "dark"
                                     :value expressions}])

(defn check-answer-button [on-click]
  (let [is-mac? (str/includes? js/navigator.appVersion "Mac")
        shortcut (if is-mac? "Cmd-E" "Ctrl-E")]
    [:button {:on-click on-click} (str "Check answer (" shortcut ")")]))

(defn result [answer solution show-result?]
  (when show-result?
    [:div
     [:span (if (= answer solution) "YES!" "NO!")]
     [:pre [:code (pr-str answer)]]]))

(defn error [error-message]
  [:div.error error-message])
