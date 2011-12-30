(ns net.orfjackal.game-of-life.visualizer-test
  (:require [net.orfjackal.game-of-life.visualizer :as v]
            [net.orfjackal.game-of-life.world :as w]
            [net.orfjackal.game-of-life.cell :as c])
  (:use clojure.test))

(deftest visualizer-tests
  (let [world (w/new-world)
        vis (v/new-visualizer world)]

    (testing "Empty world has nothing to draw"
      (is (= [] (v/cells-to-draw vis))))

    (testing "Living cells are drawn"
      (v/update! vis #(w/enliven % (c/new-cell 1 2)))
      (is (= [{:x 1 :y 2 :width 1 :height 1}] (v/cells-to-draw vis))))

    (testing "Cells are scaled according to scaling factor"
      (let [vis (v/set-scaling vis 3)]
        (v/update! vis #(w/enliven % (c/new-cell 1 2)))
        (is (= [{:x 3 :y 6 :width 3 :height 3}] (v/cells-to-draw vis)))))

    ))
