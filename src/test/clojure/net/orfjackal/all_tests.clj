(ns net.orfjackal.all-tests
  (:use net.orfjackal.experimental.hello-test
        net.orfjackal.game-of-life.world-test
        net.orfjackal.game-of-life.visualizer-test
        clojure.test))

(run-all-tests #"net\.orfjackal.*")
