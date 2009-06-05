package net.orfjackal.experimental.specs;

public class TestSpy {

    private static final ThreadLocal<StringBuffer> context = new ThreadLocal<StringBuffer>() {
        protected StringBuffer initialValue() {
            return new StringBuffer();
        }
    };

    public static void reset() {
        context.remove();
    }

    public static void append(String s) {
//        debug(s);
        context.get().append(s);
    }

    private static void debug(String s) {
        System.out.println("append: " + s);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get() {
        return context.get().toString();
    }
}
