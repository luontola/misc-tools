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

package net.orfjackal.experimental.nineplaces;

import java.util.Arrays;

/**
 * @author Esko Luontola
 * @since 9.4.2008
 */
public class Diagram {

    private static final int ROWS = 3;
    private static final int COLS = 3;
    private static final int GOAL_SUM = 15;

    private final int[] diagram;

    public Diagram() {
        this(new int[9]);
    }

    private Diagram(int[] diagram) {
        this.diagram = diagram;
    }

    public Diagram with(int x, int y, int value) {
        return with(index(x, y), value);
    }

    public Diagram with(int index, int value) {
        if (diagram[index] != 0 || contains(value, diagram)) {
            return null;
        }
        int[] copy = Arrays.copyOf(diagram, diagram.length);
        copy[index] = value;
        return new Diagram(copy);
    }

    public int length() {
        return diagram.length;
    }

    public boolean full() {
        return !contains(0, diagram);
    }

    public boolean fail() {
        return failsHorizontally() || failsVertically() || failsDiagonally();
    }

    private boolean failsHorizontally() {
        for (int y = 0; y < ROWS; y++) {
            if (failsAtPlaces(0, y, 1, y, 2, y)) {
                return true;
            }
        }
        return false;
    }

    private boolean failsVertically() {
        for (int x = 0; x < COLS; x++) {
            if (failsAtPlaces(x, 0, x, 1, x, 2)) {
                return true;
            }
        }
        return false;
    }

    private boolean failsDiagonally() {
        return failsAtPlaces(0, 0, 1, 1, 2, 2) || failsAtPlaces(2, 0, 1, 1, 0, 2);
    }

    private boolean failsAtPlaces(int x1, int y1, int x2, int y2, int x3, int y3) {
        int v1 = diagram[index(x1, y1)];
        int v2 = diagram[index(x2, y2)];
        int v3 = diagram[index(x3, y3)];
        return allSet(v1, v2, v3) && !satisfiesGoal(v1, v2, v3);
    }

    private static boolean allSet(int v1, int v2, int v3) {
        return v1 * v2 * v3 != 0;
    }

    private static boolean satisfiesGoal(int v1, int v2, int v3) {
        return v1 + v2 + v3 == GOAL_SUM;
    }

    public String toString() {
        String s = "";
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLS; x++) {
                s += diagram[index(x, y)];
            }
            s += "\n";
        }
        return s;
    }

    private static int index(int x, int y) {
        return x + y * COLS;
    }

    private static boolean contains(int needle, int[] haystack) {
        for (int value : haystack) {
            if (value == needle) {
                return true;
            }
        }
        return false;
    }
}
