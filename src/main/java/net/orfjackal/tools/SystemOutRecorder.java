/*
 * Copyright (c) 2008, Esko Luontola. All Rights Reserved.
 */

package net.orfjackal.tools;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Esko Luontola
 * @since 5.4.2008
 */
public class SystemOutRecorder {

    private PrintStream realSystemOut;
    private ByteArrayOutputStream actualBytes;
    private ByteArrayOutputStream expectedBytes;
    private PrintStream expected;

    public void install() {
        if (realSystemOut != null) {
            throw new IllegalStateException("Already installed");
        }
        realSystemOut = System.out;
        actualBytes = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualBytes));
        expectedBytes = new ByteArrayOutputStream();
        expected = new PrintStream(expectedBytes);
    }

    public void uninstall() {
        if (realSystemOut == null) {
            throw new IllegalStateException("Already uninstalled");
        }
        System.setOut(realSystemOut);
        realSystemOut = null;
        actualBytes = null;
        expectedBytes = null;
        expected = null;
    }

    public PrintStream expect() {
        return expected;
    }

    public String actualOutput() {
        return actualBytes.toString();
    }

    public String expectedOutput() {
        return expectedBytes.toString();
    }
}
