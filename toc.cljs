(ns toc
  (:require
   [clojure.string]
   [roam.datascript.reactive :as dr]
   [roam.util :as u]))

(defn block-html-el [uid]
  (some (fn [x]
          (when (clojure.string/ends-with? (.-id x) uid) x))
        (.getElementsByClassName js/document "rm-block__input")))

(defn scroll-to-block [uid]
  (when-some [el (block-html-el uid)]
    (.scrollIntoView el)))

(defn flatten-block [acc block]
  (reduce flatten-block
          (conj acc (dissoc block :block/children))
          (sort-by :block/order (:block/children block))))


(defn component [{uid :block-uid}]
  (into [:div]
        (comp
         (filter (fn [b]
                   (-> b :block/heading pos?)))
         (map (fn [b]
                [:div {:style {:margin-left (str (dec (:block/heading b)) "em")
                               :font-size (str (* 0.25 (- 7 (:block/heading b))) "em")}}
                 [:a {:on-click (fn []
                                  (scroll-to-block (:block/uid b)))}
                  (u/parse (:block/string b))]])))
        (flatten-block []
                       @(dr/q '[:find (pull ?p [:block/string :block/uid :block/heading :block/order {:block/children ...}]) .
                                :in $ ?uid
                                :where
                                [?e :block/uid ?uid]
                                [?e :block/parents ?p]
                                [?p :node/title]]
                              uid))))
