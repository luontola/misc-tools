/*
 * Copyright (c) 2008, Esko Luontola. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.orfjackal.examples;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * From http://www.javalobby.org/forums/thread.jspa?threadID=16867&tstart=0
 */
public class GameLoopExample {

    private static final int FPS = 50;
    private static final int FRAME_DELAY = 1000 / FPS;

    public static void main(String[] args) {
        Canvas gui = new Canvas();

        JFrame frame = new JFrame();
        frame.getContentPane().add(gui);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setVisible(true); // start AWT painting

        Thread gameThread = new Thread(new GameLoop(gui));
        gameThread.setPriority(Thread.MIN_PRIORITY);
        gameThread.start(); // start Game processing
    }

    private static class GameLoop implements Runnable {

        private boolean isRunning;

        private int lineX;

        private Canvas gui;

        private long targetTime;

        public GameLoop(Canvas canvas) {
            gui = canvas;
            isRunning = true;
            lineX = 0;
        }

        public void run() {
            targetTime = System.currentTimeMillis();
            gui.createBufferStrategy(2);
            BufferStrategy strategy = gui.getBufferStrategy();
            printSystemInfo(strategy);

            // Game Loop
            while (isRunning) {
                updateGameState();
                updateGUI(strategy);
                synchFramerate();
            }
        }

        private void updateGameState() {
            lineX++;
        }

        private void updateGUI(BufferStrategy strategy) {
            do {
                do {
                    Graphics g = strategy.getDrawGraphics();
                    paint(g);
                    g.dispose();
                } while (strategy.contentsRestored());
                strategy.show();
            } while (strategy.contentsLost());
        }

        private void paint(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, gui.getWidth(), gui.getHeight());
            g.setColor(Color.BLACK);

            // arbitrary rendering logic
            int x = lineX % gui.getWidth();
            int y = lineX / gui.getWidth() + 50;
            int tilt = (int) (Math.sin(lineX * 0.10) * 50);
            g.drawLine(x, y, x + 25, y + tilt);
        }

        private void synchFramerate() {
            targetTime = targetTime + FRAME_DELAY;

            // low CPU usage, maybe less accurate
            long difference = targetTime - System.currentTimeMillis();
            try {
                Thread.sleep(Math.max(0, difference));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // full CPU usage, maybe more accurate
//            while (targetTime - System.currentTimeMillis() >= 0) {
//                Thread.yield();
//            }
        }
    }

    private static void printSystemInfo(BufferStrategy strategy) {
        // Try enabling some flags: http://java.sun.com/javase/6/docs/technotes/guides/2d/flags.html
        BufferCapabilities c = strategy.getCapabilities();
        System.out.println("BufferStrategy");
        System.out.println("    isFullScreenRequired   = " + c.isFullScreenRequired());
        System.out.println("    isMultiBufferAvailable = " + c.isMultiBufferAvailable());
        System.out.println("    isPageFlipping         = " + c.isPageFlipping());
        System.out.println("BackBuffer");
        System.out.println("    isAccelerated  = " + c.getBackBufferCapabilities().isAccelerated());
        System.out.println("    isTrueVolatile = " + c.getBackBufferCapabilities().isTrueVolatile());
        System.out.println("FrontBuffer");
        System.out.println("    isAccelerated  = " + c.getFrontBufferCapabilities().isAccelerated());
        System.out.println("    isTrueVolatile = " + c.getFrontBufferCapabilities().isTrueVolatile());
    }
}
