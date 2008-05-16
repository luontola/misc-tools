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
