/*
 * Copyright (c) 2007, Esko Luontola. All Rights Reserved.
 */

package net.orfjackal.experimental;

public class AccessingAnUninitializedClass {

    public static Object obj;

    public static class Foo {
        private String foo;

        public Foo(String s) throws Exception {
            foo = s;
            obj = this;
            throw new Exception();
        }

        public String getFoo() {
            return foo;
        }
    }

    public static class Bar extends Foo {
        private String bar;

        public Bar(String s) throws Exception {
            super(s);
            bar = s;
        }

        public String getBar() {
            return bar;
        }
    }

    public static void main(String[] args) {
        Bar b = null;
        System.out.println("obj: " + obj);
        try {
            b = new Bar("Foobar");
        } catch (Exception e) {
            // NOOP
        }
        System.out.println("  b: " + b);
        System.out.println("obj: " + obj);
        System.out.println("Foo: " + ((Foo) obj).getFoo());
        System.out.println("Bar: " + ((Bar) obj).getBar());
    }
}
