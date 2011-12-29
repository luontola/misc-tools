(ns net.orfjackal.all-tests
  (:use net.orfjackal.experimental.hello-test
        net.orfjackal.experimental.game-of-life-test
        clojure.test))

(run-all-tests #"net\.orfjackal.*")
