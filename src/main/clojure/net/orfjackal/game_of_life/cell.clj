(ns net.orfjackal.game-of-life.cell)

(defn new-cell [x y]
  [x y])

(defn x [cell]
  (first cell))

(defn y [cell]
  (second cell))

(defn neighbours [cell]
  (let [x0 (x cell)
        x1 (- x0 1)
        x2 (+ x0 1)
        y0 (y cell)
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

