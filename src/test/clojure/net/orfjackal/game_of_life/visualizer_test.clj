(ns net.orfjackal.game-of-life.visualizer-test
  (:require [net.orfjackal.game-of-life.visualizer :as v]
            [net.orfjackal.game-of-life.world :as w]
            [net.orfjackal.game-of-life.cell :as c])
  (:use clojure.test))

(deftest visualizer-tests
  (let [world (w/new-world)
        scale 1]

    (testing "Empty world has nothing to draw"
      (is (= [] (v/cells-to-draw world scale))))

    (testing "Living cells are drawn"
      (let [world (w/enliven world (c/new-cell 1 2))]
        (is (= [{:x 1 :y 2 :width 1 :height 1}] (v/cells-to-draw world scale)))))

    (testing "Cells are scaled according to scaling factor"
      (let [world (w/enliven world (c/new-cell 1 2))
            scale 3]
        (is (= [{:x 3 :y 6 :width 3 :height 3}] (v/cells-to-draw world scale)))))

    (testing "Clicks on the screen are mapped to cells in the world"
      (let [scale 3]
        (is (= (c/new-cell 0 0) (v/cell-for-pixel scale 0 0)))
        (is (= (c/new-cell 0 0) (v/cell-for-pixel scale 0 2)))
        (is (= (c/new-cell 0 1) (v/cell-for-pixel scale 0 3)))
        (is (= (c/new-cell 0 0) (v/cell-for-pixel scale 2 0)))
        (is (= (c/new-cell 1 0) (v/cell-for-pixel scale 3 0)))))

    ))
