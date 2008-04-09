/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */

package net.orfjackal.experimental;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Diagram of Nine Places - Put the numbers 1 to 9 into the squares so it adds to 15
 * whether you view it horizontally, vertically or diagonally.
 * <pre>
 *   _ _ _
 *  |_|_|_|
 *  |_|_|_|
 *  |_|_|_|
 * </pre>
 * - Legend of the Condor Heroes (1982), episode 44
 * <p/>
 * Also see
 * http://www.magicl-existence.com/
 * http://www.fengshuigate.com/numerology.html
 * http://catenary.wordpress.com/2006/08/19/fun-with-representations-i-nine-numbers/
 *
 * @author Esko Luontola
 * @since 8.4.2008
 */
public class DiagramOfNinePlaces {

    private static List<Diagram> solutions = new ArrayList<Diagram>();
    private static int tries = 0;

    public static void main(String[] args) {
        Diagram diagram = new Diagram();
        search(diagram, 0);
        for (Diagram solution : solutions) {
            System.out.println(solution);
        }
        System.out.println("Total tries: " + tries);
    }

    private static void search(Diagram diagram, int nextIndex) {
        if (nextIndex < diagram.length()) {
            for (int value = 1; value <= 9; value++) {
                tries++;
                Diagram assigned = diagram.with(nextIndex, value);
                if (assigned != null) {
                    search(assigned, nextIndex + 1);
                }
            }
        } else if (diagram.full() && !diagram.fail()) {
            solutions.add(diagram);
        }
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

    public static class TestEmptyDiagram extends TestCase {

        private Diagram diagram;

        protected void setUp() throws Exception {
            diagram = new Diagram();
        }

        public void testIsNotFull() {
            assertFalse(diagram.full());
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

        public void testOverwritingAValueIsNotAllowed() {
            assertNull(diagram.with(0, 0, 2));
        }

        public void testUsingAValueTwiseIsNotAllowed() {
            assertNull(diagram.with(1, 1, 1));
        }

        public void testAlsoAnIndexMethodMayBeUsed() {
            assertEquals(original.with(0, 2).toString(), "" +
                    "200\n" +
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

    public static class TestDiagramWithDiagonalFailure extends TestCase {

        private Diagram diagram1;
        private Diagram diagram2;

        protected void setUp() throws Exception {
            diagram1 = new Diagram()
                    .with(0, 0, 1)
                    .with(1, 1, 2)
                    .with(2, 2, 3);
            diagram2 = new Diagram()
                    .with(2, 0, 1)
                    .with(1, 1, 2)
                    .with(0, 2, 3);
        }

        public void testTheValuesAreSet() {
            assertEquals(diagram1.toString(), "" +
                    "100\n" +
                    "020\n" +
                    "003\n");
            assertEquals(diagram2.toString(), "" +
                    "001\n" +
                    "020\n" +
                    "300\n");
        }

        public void testFails() {
            assertTrue(diagram1.fail());
            assertTrue(diagram2.fail());
        }
    }

    public static class TestAlmostFullDiagram extends TestCase {

        private Diagram diagram;

        protected void setUp() throws Exception {
            diagram = new Diagram()
                    .with(0, 0, 1)
                    .with(0, 1, 2)
                    .with(0, 2, 3)
                    .with(1, 0, 4)
                    .with(1, 1, 5)
                    .with(1, 2, 6)
                    .with(2, 0, 7)
                    .with(2, 1, 8);
        }

        public void testIsNotFull() {
            assertFalse(diagram.full());
        }

        public void testIsFullAfterSettingTheLastValue() {
            Diagram fullDiagram = diagram.with(2, 2, 9);
            assertTrue(fullDiagram.full());
        }
    }
}
