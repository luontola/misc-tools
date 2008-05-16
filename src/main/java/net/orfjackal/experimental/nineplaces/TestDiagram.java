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

import junit.framework.TestCase;

/**
 * @author Esko Luontola
 * @since 9.4.2008
 */
public class TestDiagram {

    public static class EmptyDiagram extends TestCase {

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

    public static class AddingAValueToADiagram extends TestCase {

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

        public void testAlsoAnIndexMayBeUsedInsteadOfXY() {
            assertEquals(original.with(0, 2).toString(), "" +
                    "200\n" +
                    "000\n" +
                    "000\n");
        }
    }

    public static class DiagramWithHorizontalFailure extends TestCase {

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

    public static class DiagramWithVerticalFailure extends TestCase {

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

    public static class DiagramWithDiagonalFailure extends TestCase {

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

    public static class AlmostFullDiagram extends TestCase {

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
