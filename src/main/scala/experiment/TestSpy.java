/*
 * Copyright (c) 2008-2009  Esko Luontola, www.orfjackal.net
 *
 * You may use and modify this source code freely for personal non-commercial use.
 * This source code may NOT be used as course material without prior written agreement.
 */

package experiment;

/**
 * @author Esko Luontola
 * @since 3.6.2009
 */
public class TestSpy {

    private static final ThreadLocal<StringBuffer> context = new ThreadLocal<StringBuffer>();

    public static void reset() {
        context.set(new StringBuffer());
    }

    public static void append(String s) {
        context.get().append(s);
    }

    public static String get() {
        return context.get().toString();
    }
}
