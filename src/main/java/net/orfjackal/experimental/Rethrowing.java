package net.orfjackal.experimental;

/**
 * : c09:Rethrowing.java
 * Demonstrating fillInStackTrace()
 * From 'Thinking in Java, 3rd ed.' (c) Bruce Eckel 2002
 * www.BruceEckel.com. See copyright notice in CopyRight.txt.
 */
public class Rethrowing {

    public static void f() throws Exception {
        System.out.println("originating the exception in f()");
        throw new Exception("thrown from f()");
    }

    public static void g() throws Throwable {
        try {
            f();
        } catch (Exception e) {
            System.err.println("Inside g(),e.printStackTrace()");
            e.printStackTrace();
//            throw e;
            throw e.fillInStackTrace();
        }
    }

    public static void main(String[] args) throws Throwable {
        try {
            g();
        } catch (Exception e) {
            System.err.println("Caught in main, e.printStackTrace()");
            e.printStackTrace();
        }
    }
}
