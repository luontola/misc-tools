/*
 * Copyright (c) 2007, Esko Luontola. All Rights Reserved.
 */

package net.orfjackal.experimental;

import net.orfjackal.tools.BenchmarkRunner;

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
        BenchmarkRunner runner = new BenchmarkRunner();

        long normalException = runner.runMeasurement(new Runnable() {
            public void run() {
                measureNormalException();
            }
        });
        long lightException = runner.runMeasurement(new Runnable() {
            public void run() {
                measureLightException();
            }
        });
        long normalExceptionMethod = runner.runMeasurement(new Runnable() {
            public void run() {
                measureNormalExceptionMethod();
            }
        });
        long lightExceptionMethod = runner.runMeasurement(new Runnable() {
            public void run() {
                measureLightExceptionMethod();
            }
        });
        long createNormalException = runner.runMeasurement(new Runnable() {
            public void run() {
                measureCreateNormalException();
            }
        });
        long createLightException = runner.runMeasurement(new Runnable() {
            public void run() {
                measureCreateLightException();
            }
        });
        long mathOperation = runner.runMeasurement(new Runnable() {
            public void run() {
                measureMathOperation();
            }
        });
        long objectCreation = runner.runMeasurement(new Runnable() {
            public void run() {
                measureObjectCreation();
            }
        });

        System.out.println();
        System.out.println("1: Throw normal exception: " + normalException + " ms");
        System.out.println("2: Throw light exception: " + lightException + " ms");
        System.out.println("3: Throw normal exception + method call: " + normalExceptionMethod + " ms");
        System.out.println("4: Throw light exception + method call: " + lightExceptionMethod + " ms");
        System.out.println("5: Call new Exception(): " + createNormalException + " ms");
        System.out.println("6: Call new LightException(): " + createLightException + " ms");
        System.out.println("7: Call Math.sin(): " + mathOperation + " ms (uses a native method)");
        System.out.println("8: Call new Object(): " + objectCreation + " ms");
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

    private static void measureNormalException() {
        try {
            throw new Exception();
        } catch (Exception e) {
            // NOOP
        }
    }

    private static void measureLightException() {
        try {
            throw new LightException();
        } catch (LightException e) {
            // NOOP
        }
    }

    private static void measureNormalExceptionMethod() {
        throwException();
    }

    private static void measureLightExceptionMethod() {
        throwLightException();
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    private static void measureCreateNormalException() {
        new Exception();
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    private static void measureCreateLightException() {
        new LightException();
    }

    private static void measureMathOperation() {
        Math.sin(123);
    }

    @SuppressWarnings({"RedundantStringConstructorCall"})
    private static void measureObjectCreation() {
        new Object();
    }
}
