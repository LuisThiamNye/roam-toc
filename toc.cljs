(ns toc
  (:require
   [clojure.string]
   [roam.block :as block]
   [roam.datascript :as d]
   [roam.datascript.reactive :as dr]
   [roam.util :as u]))

(defn ancestor-block-uids [uid]
  (d/q '[:find [?puid ...]
         :in $ ?cuid
         :where
         [?c :block/uid ?cuid]
         [?c :block/parents ?p]
         [?p :block/string]
         [?p :block/uid ?puid]]
       uid))

(defn block-html-el [uid]
  (some (fn [x]
          (when (clojure.string/ends-with? (.-id x) uid) x))
        (.getElementsByClassName js/document "rm-block__input")))

(defn try-scroll-into-view [uid]
  (when-some [el (block-html-el uid)]
      (.scrollIntoView el)))

(defn scroll-to-block [uid]
  (doseq [parent (ancestor-block-uids uid)]
    (block/update
     {:block {:uid parent :open true}}))
  (when-not (try-scroll-into-view uid)
    (js/setTimeout #(try-scroll-into-view uid) 300)))

(defn flatten-block [acc block]
  (reduce flatten-block
          (conj acc (dissoc block :block/children))
          (sort-by :block/order (:block/children block))))


(defn block->table-entry [block]
  [:div {:style {:margin-left (str (dec (:block/heading block)) "em")
                 :font-size (str (* 0.25 (- 7 (:block/heading block)))
                                 "em")}}
   [:a {:on-click #(scroll-to-block (:block/uid block))}
    (u/parse (:block/string block))]])

(defn component [{uid :block-uid}]
  (let [block @(dr/q '[:find (pull ?p [:block/string
                                       :block/uid
                                       :block/heading
                                       :block/order
                                       {:block/children ...}]) .
                       :in $ ?uid
                       :where
                       [?e :block/uid ?uid]
                       [?e :block/parents ?p]
                       [?p :node/title]]
                     uid)]
    (into [:div]
          (comp
           (filter #(-> % :block/heading pos?))
           (map block->table-entry))
          (flatten-block [] block))))
