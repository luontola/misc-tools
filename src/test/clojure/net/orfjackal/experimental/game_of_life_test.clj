(ns net.orfjackal.experimental.game-of-life-test
  (:use net.orfjackal.experimental.game-of-life
        clojure.test))

(defn set-live-neighbours [world cell neighbour-count]
  (let [neighbours-to-liven (shuffle (take neighbour-count (neighbours cell)))]
    (reduce #(enliven %1 %2) world neighbours-to-liven)))

(deftest game-of-life-tests
  (let [cell (new-cell 0 0)
        other-cell (new-cell 0 1)
        world (new-world)]

    (testing "At first all cells are dead (i.e. not live)"
      (is (dead? world cell))
      (is (not (live? world cell))))

    (testing "When a cell is made live,"
      (let [world (enliven world cell)]

        (testing "then it is live (i.e. not dead)"
          (is (live? world cell))
          (is (not (dead? world cell))))

        (testing "other cells are unaffected"
          (is (dead? world other-cell)))))

    (testing "On tick, any live cell"
      (let [world (enliven world cell)]

        (testing "with < 2 live neighbours dies"
          (is (dead? (tick (set-live-neighbours world cell 0)) cell))
          (is (dead? (tick (set-live-neighbours world cell 1)) cell)))

        (testing "with 2 or 3 live neighbours lives on"
          (is (live? (tick (set-live-neighbours world cell 2)) cell))
          (is (live? (tick (set-live-neighbours world cell 3)) cell)))

        (testing "with > 3 live neighbours dies"
          (is (dead? (tick (set-live-neighbours world cell 4)) cell))
          (is (dead? (tick (set-live-neighbours world cell 5)) cell))
          (is (dead? (tick (set-live-neighbours world cell 6)) cell))
          (is (dead? (tick (set-live-neighbours world cell 7)) cell))
          (is (dead? (tick (set-live-neighbours world cell 8)) cell)))))

    (testing "On tick, any dead cell with 3 live neighbours comes to life"
      (is (live? (tick (set-live-neighbours world cell 3)) cell)))

    (testing "On tick, all other dead cells stay dead"
      (is (dead? (tick (set-live-neighbours world cell 0)) cell))
      (is (dead? (tick (set-live-neighbours world cell 1)) cell))
      (is (dead? (tick (set-live-neighbours world cell 2)) cell))
      (is (dead? (tick (set-live-neighbours world cell 4)) cell))
      (is (dead? (tick (set-live-neighbours world cell 5)) cell))
      (is (dead? (tick (set-live-neighbours world cell 6)) cell))
      (is (dead? (tick (set-live-neighbours world cell 7)) cell))
      (is (dead? (tick (set-live-neighbours world cell 8)) cell)))


    (testing "Util: get neighbours"
      (is (= #{(new-cell 14 24)
               (new-cell 14 25)
               (new-cell 14 26)
               (new-cell 15 24)
               (new-cell 15 26)
               (new-cell 16 24)
               (new-cell 16 25)
               (new-cell 16 26)}
            (neighbours (new-cell 15 25)))))

    (testing "Util: count live neighbours"
      (let [world (enliven world (new-cell 1 0))
            world (enliven world (new-cell 0 1))]
        (is (= 2 (live-neighbours-count world (new-cell 0 0))))))

    (testing "Util: make a number of neighbours live"
      (let [world (set-live-neighbours world cell 4)]
        (is (= 4 (live-neighbours-count world cell)))))

    (testing "Util: fiddly cells"
      (is (= 0 (count (fiddly-cells world))))
      (is (= 9 (count (fiddly-cells (enliven world cell))))))

    ))
