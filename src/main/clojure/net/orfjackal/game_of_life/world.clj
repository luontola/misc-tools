(ns net.orfjackal.game-of-life.world
  (:require [net.orfjackal.game-of-life.cell :as c]
            [clojure.set :as set])
  (:use net.orfjackal.clj-utils))

(defn new-world []
    #{})

(defn live? [world cell]
  (contains? world cell))

(def dead? (comp not live?))

(defn enliven [world cell]
  (conj world cell))

(defn kill [world cell]
  (disj world cell))

(defn- set-liveness [world cell liveness]
  (if liveness
    (enliven world cell)
    (kill world cell)))

(defn live-cells [world]
  world)

(defn fiddly-cells [world]
  (reduce #(set/union %1 (c/neighbours %2)) world (live-cells world)))

(defn live-neighbours-count [world cell]
  (count-if #(live? world %) (c/neighbours cell)))

(defn- will-live? [world cell]
  (let [n (live-neighbours-count world cell)]
    (if (live? world cell)
      (or (= n 2) (= n 3))
      (= n 3))))

(defn tick [world]
  (reduce
    #(set-liveness %1 %2 (will-live? world %2))
    world (fiddly-cells world)))
