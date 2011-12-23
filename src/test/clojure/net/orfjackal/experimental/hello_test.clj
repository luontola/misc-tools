(ns net.orfjackal.experimental.hello-test
  (:use net.orfjackal.experimental.hello)
  (:use clojure.test))

(deftest say-hello-world
  (is (= "Hello World" (say-hello "World"))))

(deftest add-1-to-2
  (is (= 3 (+ 1 2))))
