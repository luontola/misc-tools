(ns net.orfjackal.game-of-life.benchmark
  (:gen-class :extends com.google.caliper.SimpleBenchmark
              :methods [;[timeNanoTime [int] void]
                        ;[timeCurrentTimeMillis [int] void]
                        [timeTickWorld [int] int]])
  (:require [net.orfjackal.game-of-life.world :as w]
            [net.orfjackal.game-of-life.cell :as c]
            [net.orfjackal.game-of-life.visualizer :as v])
  (:import [com.google.caliper SimpleBenchmark]))

(defn -timeNanoTime [this reps]
  (dotimes [n reps] (System/nanoTime)))

(defn -timeCurrentTimeMillis [this reps]
  (dotimes [n reps] (System/currentTimeMillis)))


; "Diehard" is a pattern that eventually disappears (rather than merely stabilize) after 130 generations,
; which is conjectured to be maximal for patterns with seven or fewer cells.
; - http://en.wikipedia.org/wiki/Conway's_Game_of_Life#Examples_of_patterns
(defn- add-diehard [world]
  (let [world (w/enliven world (c/new-cell 1 2))
        world (w/enliven world (c/new-cell 2 2))
        world (w/enliven world (c/new-cell 2 3))

        world (w/enliven world (c/new-cell 6 3))
        world (w/enliven world (c/new-cell 7 1))
        world (w/enliven world (c/new-cell 7 3))
        world (w/enliven world (c/new-cell 8 3))]
    world))

(def initial-world (add-diehard (w/new-world)))
(def generations-to-tick 130)

(defn -timeTickWorld [this reps]
  (let [junk (atom 0)]
    (dotimes [n reps]

      (let [world (atom initial-world)]
        (dotimes [generation generations-to-tick]
          (swap! world w/tick))

        (swap! junk + (count (w/live-cells (deref world))))))
    (deref junk)))
