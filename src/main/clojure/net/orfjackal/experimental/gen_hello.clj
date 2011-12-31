(ns net.orfjackal.experimental.gen-hello
  (:gen-class))

(defn -main
  [greetee]
  (println (str "Hello " greetee "!")))
