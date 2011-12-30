(ns net.orfjackal.game-of-life.visualizer
  (:require [net.orfjackal.game-of-life.world :as w]
            [net.orfjackal.game-of-life.cell :as c]))

(defn- cell-to-pixel [scale cell-value]
  (* scale cell-value))

(defn- pixel-to-cell [scale pixel-value]
  (int (/ pixel-value scale)))

(defn cells-to-draw [world scale]
  (let [x (fn [cell] (cell-to-pixel scale (c/x cell)))
        y (fn [cell] (cell-to-pixel scale (c/y cell)))
        width scale
        height scale]
    (map
      (fn [cell] {:x (x cell) :y (y cell) :width width :height height})
      (w/live-cells world))))

(defn cell-for-pixel [scale pixel-x pixel-y]
  (let [cell-x (pixel-to-cell scale pixel-x)
        cell-y (pixel-to-cell scale pixel-y)]
    (c/new-cell cell-x cell-y)))
