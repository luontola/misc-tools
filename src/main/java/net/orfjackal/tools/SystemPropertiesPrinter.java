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

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Prints all system properties and their default values in the current system. The value of the
 * <code>"line.separator"</code> property will be escaped for better visibility. See the documentation of {@link
 * System#getProperties()} for a list of properties that are always present.
 *
 * @author Esko Luontola
 * @see System#getProperties()
 * @since 27.1.2006
 */
public class SystemPropertiesPrinter {

    public static void main(String[] args) {

        List<String> keys = new ArrayList<String>();
        for (Object o : System.getProperties().keySet()) {
            keys.add(o.toString());
        }
        Collections.sort(keys);

        for (String key : keys) {
            String value = System.getProperty(key);
            if (key.equals("line.separator")) {
                value = escape(value);
            }
            System.out.println(key + " = " + value);
        }
    }

    /**
     * Escapes all line sepatators in a string. CR and LF will be replaced with <code>\r</code> and <code>\n</code>. All
     * other characters will be escaped using the <code>\u0000</code> notation.
     */
    private static String escape(String value) {
        StringCharacterIterator iter = new StringCharacterIterator(value);
        value = "";
        for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
            if (c == '\r') {
                value += "\\r";
            } else if (c == '\n') {
                value += "\\n";
            } else {
                // escape the Unicode line separators u0085, u000C, u2028 and u2029
                String s = Integer.toHexString(c).toUpperCase();
                while (s.length() < 4) {
                    s = "0" + s;
                }
                value += "\\u" + s;
            }
        }
        return value;
    }
}
