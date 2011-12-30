(ns net.orfjackal.all-tests
  (:use net.orfjackal.experimental.hello-test
        net.orfjackal.game-of-life.world-test
        clojure.test))

(run-all-tests #"net\.orfjackal.*")
