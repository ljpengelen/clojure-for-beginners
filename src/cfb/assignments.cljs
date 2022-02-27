(ns cfb.assignments)

(defn random-numbers
  ([] (random-numbers 2000))
  ([m] (repeatedly 20 #(rand-int m))))

(defn reverse-list []
  (let [x (random-numbers)]
    {:description
     [:<>
      [:p
       "Welcome to Clojure for beginners! "
       "You'll be given a number of questions, one by one, and a code editor for writing your solution for each of them. "
       "There's a button and a shortcut you can use to check your answer at any time. "]
      [:p
       "The var " [:code "x"] " below is bound to a list of random numbers. "
       "Reverse this list by replacing the underscore."]
      [:p "You can use " [:a {:href "https://clojuredocs.org/"} "ClojureDocs"] " to search for useful functions."]]
     :expressions (str
                   "(def x '" x ")\n\n"
                   "(_ x)")
     :solution (reverse x)}))

(defn core-link [f]
  [:a {:href (str "https://clojuredocs.org/clojure.core/" f)} f])

(defn find-max []
  (let [x (random-numbers)]
    {:description
     [:<>
      [:p
       "The var " [:code "x"] " below is bound to a list of random numbers. "
       "Find the largest number by replacing the underscore."]
      [:p
       "The function " (core-link "max") " takes a variable number of arguments. "
       "Applying max to a single argument will just return that argument."]]
     :expressions (str
                   "(def x '" x ")\n\n"
                   "(_ max x)")
     :solution (apply max x)}))

(defn find-second-largest []
  (let [x (random-numbers)]
    {:description
     [:<>
      [:p
       "The var " [:code "x"] " below is bound to a list of random numbers. "
       "Find the second largest number."]
      [:p
       "The expression below uses Clojure's " (core-link "sort") " function, which returns a sorted sequence. "
       "Clojure offers a number of functions to take certain elements or subsets from collections and sequences, "
       "such as " (core-link "first") ", " (core-link "second") ", " (core-link "rest") ", and " (core-link "next") "."]]
     :expressions (str
                   "(def x '" x ")\n\n"
                   "(_ (_ (sort > x)))")
     :solution (->> x
                    (sort >)
                    next
                    first)}))

(defn second-smallest-even []
  (let [x (random-numbers)]
    {:description
     [:<>
      [:p
       "The var " [:code "x"] " below is bound to a list of random numbers. "
       "Find the second smallest even number."]]
     :expressions (str
                   "(def x '" x ")\n\n"
                   "(->> x\n"
                   "     (filter _)\n"
                   "     _\n"
                   "     _)")
     :solution (->> x
                    (filter even?)
                    sort
                    second)}))

(def assignments
  [(reverse-list)
   (find-max)
   (find-second-largest)
   (second-smallest-even)])
