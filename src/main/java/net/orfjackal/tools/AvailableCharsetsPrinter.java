/*
 * Copyright (c) 2006, Esko Luontola. All Rights Reserved.
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
