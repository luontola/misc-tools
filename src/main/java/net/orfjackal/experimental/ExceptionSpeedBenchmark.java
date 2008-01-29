/*
 * Copyright (c) 2007, Esko Luontola. All Rights Reserved.
 */

package net.orfjackal.experimental;

import java.util.Scanner;

/**
 * Benchmarks the speed of throwing exceptions with and without stack trace.
 *
 * @author Esko Luontola
 * @since 25.1.2007
 */
public class ExceptionSpeedBenchmark {

    private static class LightException extends Throwable {

        public synchronized Throwable fillInStackTrace() {
            return this;
        }

    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter number of repeats: ");
        int repeats = in.nextInt();
        System.out.println();

        long normalException = measureNormalException(repeats);
        long lightException = measureLightException(repeats);
        long normalExceptionMethod = measureNormalExceptionMethod(repeats);
        long lightExceptionMethod = measureLightExceptionMethod(repeats);
        long createNormalException = measureCreateNormalException(repeats);
        long createLightException = measureCreateLightException(repeats);
        long mathOperation = measureMathOperation(repeats);
        long objectCreation = measureObjectCreation(repeats);

        System.out.println("1: Throw normal exception: " + normalException + " ms");
        System.out.println("2: Throw light exception: " + lightException + " ms");
        System.out.println("3: Throw normal exception + method call: " + normalExceptionMethod + " ms");
        System.out.println("4: Throw light exception + method call: " + lightExceptionMethod + " ms");
        System.out.println("5: Call new Exception(): " + createNormalException + " ms");
        System.out.println("6: Call new LightException(): " + createLightException + " ms");
        System.out.println("7: Call Math.sin(): " + mathOperation + " ms (uses a native method)");
        System.out.println("8: Call new String(): " + objectCreation + " ms");
        System.out.println();

        long methodOverhead = lightExceptionMethod - lightException;
        long exceptionMethodOverhead = normalExceptionMethod - normalException - methodOverhead;
        double lightExceptionSpeedup1 = ((double) normalException) / lightException;
        double lightExceptionSpeedup2 = ((double) normalExceptionMethod) / lightExceptionMethod;
        long throwNormalException = normalException - createNormalException;
        long throwLightException = lightException - createLightException;
        System.out.println("Method call overhead: " + methodOverhead + " ms");
        System.out.println("Stack trace overhead for a method call: " + exceptionMethodOverhead + " ms");
        System.out.println("Light exceptions compared to normal exceptions: "
                + String.format("%.2f to %.2f", lightExceptionSpeedup1, lightExceptionSpeedup2) + " times faster");
        System.out.println("Throw normal exception (init time subtracted): " + throwNormalException + " ms");
        System.out.println("Throw light exception (init time subtracted): " + throwLightException + " ms");
    }

    private static void throwLightException() {
        try {
            throw new LightException();
        } catch (LightException e) {
            // NOOP
        }
    }

    private static void throwException() {
        try {
            throw new Exception();
        } catch (Exception e) {
            // NOOP
        }
    }

    // TESTS

    private static long measureNormalException(int repeats) {
        long start;
        long end;
        start = System.currentTimeMillis();
        for (int i = 0; i < repeats; i++) {
            try {
                throw new Exception();
            } catch (Exception e) {
                // NOOP
            }
        }
        end = System.currentTimeMillis();
        return end - start;
    }

    private static long measureLightException(int repeats) {
        long start;
        long end;
        start = System.currentTimeMillis();
        for (int i = 0; i < repeats; i++) {
            try {
                throw new LightException();
            } catch (LightException e) {
                // NOOP
            }
        }
        end = System.currentTimeMillis();
        return end - start;
    }

    private static long measureNormalExceptionMethod(int repeats) {
        long start;
        long end;
        start = System.currentTimeMillis();
        for (int i = 0; i < repeats; i++) {
            throwException();
        }
        end = System.currentTimeMillis();
        return end - start;
    }

    private static long measureLightExceptionMethod(int repeats) {
        long start;
        long end;
        start = System.currentTimeMillis();
        for (int i = 0; i < repeats; i++) {
            throwLightException();
        }
        end = System.currentTimeMillis();
        return end - start;
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    private static long measureCreateNormalException(int repeats) {
        long start;
        long end;
        start = System.currentTimeMillis();
        for (int i = 0; i < repeats; i++) {
            new Exception();
        }
        end = System.currentTimeMillis();
        return end - start;
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    private static long measureCreateLightException(int repeats) {
        long start;
        long end;
        start = System.currentTimeMillis();
        for (int i = 0; i < repeats; i++) {
            new LightException();
        }
        end = System.currentTimeMillis();
        return end - start;
    }

    private static long measureMathOperation(int repeats) {
        long start;
        long end;
        start = System.currentTimeMillis();
        for (int i = 0; i < repeats; i++) {
            Math.sin(i);
        }
        end = System.currentTimeMillis();
        return end - start;
    }

    @SuppressWarnings({"RedundantStringConstructorCall"})
    private static long measureObjectCreation(int repeats) {
        long start;
        long end;
        start = System.currentTimeMillis();
        for (int i = 0; i < repeats; i++) {
            new Object();
        }
        end = System.currentTimeMillis();
        return end - start;
    }
}
