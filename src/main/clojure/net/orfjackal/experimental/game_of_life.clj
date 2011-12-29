(ns net.orfjackal.experimental.game-of-life
  (:use clojure.set))

; utils

(def count-if (comp count filter))


; cell

(defn new-cell [x y]
  [x y])

(defn neighbours [cell]
  (let [x0 (first cell)
        x1 (- x0 1)
        x2 (+ x0 1)
        y0 (second cell)
        y1 (- y0 1)
        y2 (+ y0 1)]
      #{
      (new-cell x0 y1)
      (new-cell x0 y2)
      (new-cell x1 y0)
      (new-cell x1 y1)
      (new-cell x1 y2)
      (new-cell x2 y0)
      (new-cell x2 y1)
      (new-cell x2 y2)
      }))

; world

(defn new-world []
    #{})

(defn live? [world cell]
  (contains? world cell))

(def dead? (comp not live?))

(defn enliven [world cell]
  (conj world cell))

(defn kill [world cell]
  (disj world cell))

(defn set-liveness [world cell liveness]
  (if liveness
    (enliven world cell)
    (kill world cell)))

(defn live-cells [world]
  world)

(defn fiddly-cells [world]
  (reduce #(union %1 (neighbours %2)) world (live-cells world)))

(defn live-neighbours-count [world cell]
  (count-if #(live? world %) (neighbours cell)))

(defn will-live? [world cell]
  (let [n (live-neighbours-count world cell)]
    (if (live? world cell)
      (or (= n 2) (= n 3))
      (= n 3))))

(defn tick [world]
  (reduce
    #(set-liveness %1 %2 (will-live? world %2))
    world (fiddly-cells world)))
