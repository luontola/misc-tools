/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */

package net.orfjackal.experimental;

import junit.framework.TestCase;

import java.util.Arrays;

/**
 * Diagram of Nine Places - Put the numbers 1 to 9 into the squares so it adds to 15
 * whether you view it horizontally, vertically or diagonally.
 * <pre>
 *   _ _ _
 *  |_|_|_|
 *  |_|_|_|
 *  |_|_|_|
 * </pre>
 *
 * @author Esko Luontola
 * @since 8.4.2008
 */
public class DiagramOfNinePlaces {

    public static void main(String[] args) {

    }

    private static class Diagram {

        public static final int ROWS = 3;
        public static final int COLS = 3;
        private static final int GOAL_SUM = 15;

        private final int[] diagram;

        private Diagram() {
            this(new int[9]);
        }

        private Diagram(int[] diagram) {
            this.diagram = diagram;
        }

        public Diagram with(int x, int y, int value) {
            int[] copy = Arrays.copyOf(diagram, diagram.length);
            copy[index(x, y)] = value;
            return new Diagram(copy);
        }

        private static int index(int x, int y) {
            return x + y * COLS;
        }

        public boolean complete() {
            return false;
        }

        public boolean fail() {
            return failsHorizontally() || failsVertically();
        }

        private boolean failsHorizontally() {
            for (int y = 0; y < ROWS; y++) {
                if (failsAtIndexes(0, y, 1, y, 2, y)) {
                    return true;
                }
            }
            return false;
        }

        private boolean failsVertically() {
            for (int x = 0; x < COLS; x++) {
                if (failsAtIndexes(x, 0, x, 1, x, 2)) {
                    return true;
                }
            }
            return false;
        }

        private boolean failsAtIndexes(int x1, int y1, int x2, int y2, int x3, int y3) {
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
    }

    public static class TestEmptyDiagram extends TestCase {

        private Diagram diagram;

        protected void setUp() throws Exception {
            diagram = new Diagram();
        }

        public void testIsNotComplete() {
            assertFalse(diagram.complete());
        }

        public void testDoesNotFail() {
            assertFalse(diagram.fail());
        }

        public void testNoValuesAreSet() {
            assertEquals(diagram.toString(), "" +
                    "000\n" +
                    "000\n" +
                    "000\n");
        }
    }

    public static class TestWhenAValueIsSet extends TestCase {

        private Diagram original;
        private Diagram diagram;

        protected void setUp() throws Exception {
            original = new Diagram();
            diagram = original.with(0, 0, 1);
        }

        public void testTheValueIsSet() {
            assertEquals(diagram.toString(), "" +
                    "100\n" +
                    "000\n" +
                    "000\n");
        }

        public void testTheOriginalDiagramIsNotModified() {
            assertEquals(original.toString(), "" +
                    "000\n" +
                    "000\n" +
                    "000\n");
        }
    }

    public static class TestDiagramWithHorizontalFailure extends TestCase {

        private Diagram diagram;

        protected void setUp() throws Exception {
            diagram = new Diagram()
                    .with(0, 0, 1)
                    .with(1, 0, 2)
                    .with(2, 0, 3);
        }

        public void testTheValuesAreSet() {
            assertEquals(diagram.toString(), "" +
                    "123\n" +
                    "000\n" +
                    "000\n");

        }

        public void testFails() {
            assertTrue(diagram.fail());
        }
    }

    public static class TestDiagramWithVerticalFailure extends TestCase {

        private Diagram diagram;

        protected void setUp() throws Exception {
            diagram = new Diagram()
                    .with(0, 0, 1)
                    .with(0, 1, 2)
                    .with(0, 2, 3);
        }

        public void testTheValuesAreSet() {
            assertEquals(diagram.toString(), "" +
                    "100\n" +
                    "200\n" +
                    "300\n");

        }

        public void testFails() {
            assertTrue(diagram.fail());
        }
    }
}
