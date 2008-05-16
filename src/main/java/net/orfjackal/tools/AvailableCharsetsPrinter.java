/*
 * Copyright (c) 2006, Esko Luontola. All Rights Reserved.
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

import java.nio.charset.Charset;
import java.util.SortedMap;

/**
 * Prints the charsets that are available in the current Java Runtime Environment. Also reports the default line
 * separator and charset.
 *
 * @author Esko Luontola
 * @see Charset
 * @since 27.1.2006
 */
public class AvailableCharsetsPrinter {

    /**
     * See <a href="http://en.wikipedia.org/wiki/Newline#Unicode">http://en.wikipedia.org/wiki/Newline#Unicode</a> for a
     * list of newline characters supported by the Unicode specification.
     */
    private static final String[] LINE_SEPARATOR = new String[]{
            "\n",
            "\r",
            "\r\n",
            "\u0085",
            "\u000C",
            "\u2028",
            "\u2029"
    };
    private static final String[] LINE_SEPARATOR_NAME = new String[]{
            "\\n (Line Feed, UNIX)",
            "\\r (Carriage Return, Macintosh)",
            "\\r\\n (CR+LF, MS-DOS)",
            "\\u0085 (Next Line)",
            "\\u000C (Form Feed)",
            "\\u2028 (Line Separator)",
            "\\u2029 (Paragraph Separator)"
    };

    public static void main(String[] args) {

        System.out.println("Default Charset:");
        printCharset(Charset.defaultCharset());
        System.out.println();

        System.out.println("Line Separator:");
        String lineSeparator = System.getProperty("line.separator");
        for (int i = 0; i < LINE_SEPARATOR.length; i++) {
            if (lineSeparator.equals(LINE_SEPARATOR[i])) {
                System.out.println(LINE_SEPARATOR_NAME[i]);
                break;
            }
        }
        System.out.println();

        SortedMap<String, Charset> charsets = Charset.availableCharsets();
        System.out.println("Available Charsets (total of " + charsets.size() + "):");
        for (Charset charset : charsets.values()) {
            printCharset(charset);
        }
    }

    private static void printCharset(Charset charset) {
        String name = charset.name();
        String displayName = charset.displayName();
        if (name.equals(displayName)) {
            System.out.println(name);
        } else {
            System.out.println(name + "\t(" + displayName + ")");
        }
        for (String s : charset.aliases()) {
            System.out.print("\t" + s);
        }
        System.out.println();
    }
}
