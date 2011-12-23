(ns net.orfjackal.all-tests
  (:use net.orfjackal.experimental.hello-test)
  (:use clojure.test))

(run-all-tests #"net\.orfjackal.*")
