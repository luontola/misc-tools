(ns net.orfjackal.game-of-life.visualizer
  (:require [net.orfjackal.game-of-life.world :as w]
            [net.orfjackal.game-of-life.cell :as c]))

(defn new-visualizer [world]
  {:world (atom world) :scaling 1})

(defn set-scaling [vis scaling]
  (assoc vis :scaling scaling))

(defn get-world [vis]
  (deref (:world vis)))

(defn update! [vis f-on-world]
  (swap! (:world vis) f-on-world))

(defn cells-to-draw [vis]
  (let [scaling (:scaling vis)
        x (fn [cell] (* scaling (c/x cell)))
        y (fn [cell] (* scaling (c/y cell)))
        width scaling
        height scaling]
    (map
      (fn [cell] {:x (x cell) :y (y cell) :width width :height height})
      (w/live-cells (get-world vis)))))
