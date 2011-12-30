(ns net.orfjackal.game-of-life.gui
  (:require [net.orfjackal.game-of-life.world :as w]
            [net.orfjackal.game-of-life.cell :as c]
            [net.orfjackal.game-of-life.visualizer :as v])
  (:import [javax.swing SwingUtilities JFrame JLabel JPanel JButton]
           [java.awt.event ActionListener]
           [java.awt BorderLayout Graphics2D Color]))

; http://stuartsierra.com/2010/01/03/doto-swing-with-clojure
;(defmacro on-action [component event & body]
;  `(. ~component addActionListener
;     (proxy [java.awt.event.ActionListener] []
;       (actionPerformed [~event] ~@body))))

(defn start-gui []
  (let [world (w/new-world)

        ; Glider
        world (w/enliven world (c/new-cell 1 3))
        world (w/enliven world (c/new-cell 2 3))
        world (w/enliven world (c/new-cell 3 3))
        world (w/enliven world (c/new-cell 3 2))
        world (w/enliven world (c/new-cell 2 1))

        visualizer (v/new-visualizer world)
        visualizer (v/set-scaling visualizer 3)

        visualizer-panel
        (doto (proxy [JPanel] []
                (paintComponent [g]
                  (proxy-super paintComponent g)
                  (doto g
                    (.setColor Color/BLACK))
                  (doall (map
                           (fn [cell] (.fillRect g (:x cell) (:y cell) (:width cell) (:height cell)))
                           (v/cells-to-draw visualizer)))))
          (.setBackground Color/WHITE))

        tick-button
        (doto (JButton. "Tick")
          (.addActionListener
            (proxy [ActionListener] []
              (actionPerformed [event]
                (v/update! visualizer w/tick)
                (.repaint visualizer-panel)))))

        controls-panel
        (doto (JPanel.)
          (.add tick-button))

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
