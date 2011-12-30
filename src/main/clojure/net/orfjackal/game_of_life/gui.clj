(ns net.orfjackal.game-of-life.gui
  (:require [net.orfjackal.game-of-life.world :as w]
            [net.orfjackal.game-of-life.cell :as c]
            [net.orfjackal.game-of-life.visualizer :as v])
  (:import [javax.swing SwingUtilities JFrame JLabel JPanel JButton]
           [java.awt.event ActionListener]
           [java.awt BorderLayout]))

; http://stuartsierra.com/2010/01/03/doto-swing-with-clojure
;(defmacro on-action [component event & body]
;  `(. ~component addActionListener
;     (proxy [java.awt.event.ActionListener] []
;       (actionPerformed [~event] ~@body))))

(defn start-gui []
  (let [world (w/new-world)
        world (w/enliven world (c/new-cell 0 1))
        world (w/enliven world (c/new-cell 0 2))
        world (w/enliven world (c/new-cell 0 3))
        visualizer (v/new-visualizer world)

        temp-label (JLabel. "")
        tick-button
        (doto (JButton. "Tick")
          ; (on-action event (println (str "Action: " event)))
          (.addActionListener
            (proxy [ActionListener] []
              (actionPerformed [event]
                (v/update! visualizer w/tick)
                (.setText temp-label (str (v/get-world visualizer)))))))

        controls-panel
        (doto (JPanel.)
          (.setOpaque true)
          (.add temp-label)
          (.add tick-button))

        visualizer-panel
        (doto (JPanel.))

        content-pane
        (doto (JPanel.)
          (.setLayout (BorderLayout.))
          (.add visualizer-panel BorderLayout/CENTER)
          (.add controls-panel BorderLayout/SOUTH))
        ]
    (doto (JFrame. "Conway's Game of Life")
      (.setContentPane content-pane)
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
      (.setSize 700 500)
      (.setLocation 300 300)
      (.setVisible true))))

(SwingUtilities/invokeLater (fn [] (start-gui)))
