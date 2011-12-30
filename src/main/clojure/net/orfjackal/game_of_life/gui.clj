(ns net.orfjackal.game-of-life.gui
  (:use net.orfjackal.game-of-life.world)
  (:import (javax.swing SwingUtilities JFrame JLabel JPanel JButton)
           (java.awt.event ActionListener)))

; http://stuartsierra.com/2010/01/03/doto-swing-with-clojure
;(defmacro on-action [component event & body]
;  `(. ~component addActionListener
;     (proxy [java.awt.event.ActionListener] []
;       (actionPerformed [~event] ~@body))))

(defn start-gui []
  (let [world (new-world)
        world (enliven world (new-cell 0 1))
        world (enliven world (new-cell 0 2))
        world (enliven world (new-cell 0 3))
        world (atom world)

        label (JLabel. "")
        button
        (doto (JButton. "Tick")
          ; (on-action event (println (str "Action: " event)))
          (.addActionListener
            (proxy [ActionListener] []
              (actionPerformed [event]
                (.setText label (str (swap! world tick)))))))
        panel
        (doto (JPanel.)
          (.setOpaque true)
          (.add label)
          (.add button))]
    (doto (JFrame. "Conway's Game of Life")
      (.setContentPane panel)
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
      (.setSize 700 500)
      (.setLocation 300 300)
      (.setVisible true))))

(SwingUtilities/invokeLater (fn [] (start-gui)))

