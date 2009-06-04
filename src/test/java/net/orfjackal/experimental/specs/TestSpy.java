package net.orfjackal.experimental.specs;

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
