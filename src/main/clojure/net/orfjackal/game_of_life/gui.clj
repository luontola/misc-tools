(ns net.orfjackal.game-of-life.gui
  (:require [net.orfjackal.game-of-life.world :as w]
            [net.orfjackal.game-of-life.cell :as c]
            [net.orfjackal.game-of-life.visualizer :as v])
  (:import [javax.swing SwingUtilities JFrame JLabel JPanel JButton Timer]
           [java.awt.event ActionListener MouseListener MouseEvent]
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

        scale 5
        world (atom world)
        ticker (Timer. 500 nil)

        ; layout components

        world-panel
        (doto (proxy [JPanel] []
                (paintComponent [g]
                  (proxy-super paintComponent g)
                  (doto g
                    (.setColor Color/BLACK))
                  (doall (map
                           (fn [cell] (.fillRect g (:x cell) (:y cell) (:width cell) (:height cell)))
                           (v/cells-to-draw (deref world) scale)))))
          (.setBackground Color/WHITE))

        start-stop-button
        (doto (JButton. "Start"))

        step-button
        (doto (JButton. "Step"))

        reset-button
        (doto (JButton. "Reset")
          (.setForeground Color/RED))

        controls-panel
        (doto (JPanel.)
          (.add start-stop-button)
          (.add step-button)
          (.add reset-button))

        content-pane
        (doto (JPanel.)
          (.setLayout (BorderLayout.))
          (.add world-panel BorderLayout/CENTER)
          (.add controls-panel BorderLayout/SOUTH))]

    ; operations

    (defn update-world! [f]
      (swap! world f)
      (.repaint world-panel))

    (defn tick! []
      (update-world! w/tick))

    (defn start-ticker! []
      (.start ticker)
      (.setText start-stop-button "Stop"))

    (defn stop-ticker! []
      (.stop ticker)
      (.setText start-stop-button "Start"))

    (defn reset-world! []
      (stop-ticker!)
      (update-world! (fn [world] (w/new-world))))

    ; register listeners

    (doto world-panel
      (.addMouseListener
        (proxy [MouseListener] []
          (mouseClicked [event])
          (mouseEntered [event])
          (mouseExited [event])
          (mousePressed [event]
            (let [clicked-cell (v/cell-for-pixel scale (.getX event) (.getY event))
                  enliven-or-kill (if (= MouseEvent/BUTTON1 (.getButton event)) w/enliven w/kill)]
              (update-world! #(enliven-or-kill % clicked-cell))))
          (mouseReleased [event]))))

    (doto ticker
      (.addActionListener
        (proxy [ActionListener] []
          (actionPerformed [event]
            (tick!)))))

    (doto start-stop-button
      (.addActionListener
        (proxy [ActionListener] []
          (actionPerformed [event]
            (if (.isRunning ticker)
              (stop-ticker!)
              (start-ticker!))))))

    (doto step-button
      (.addActionListener
        (proxy [ActionListener] []
          (actionPerformed [event]
            (stop-ticker!)
            (tick!)))))

    (doto reset-button
      (.addActionListener
        (proxy [ActionListener] []
          (actionPerformed [event]
            (reset-world!)))))

    ; create frame

    (doto (JFrame. "Conway's Game of Life")
      (.setContentPane content-pane)
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
      (.setSize 700 500)
      (.setLocation 300 300)
      (.setVisible true))))

(SwingUtilities/invokeLater (fn [] (start-gui)))
