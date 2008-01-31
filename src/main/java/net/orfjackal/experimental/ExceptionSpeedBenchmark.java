/*
 * Copyright (c) 2007, Esko Luontola. All Rights Reserved.
 */

package net.orfjackal.experimental;

import net.orfjackal.tools.Benchmark;

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
        Benchmark runner = new Benchmark();

        Benchmark.Results normalException = runner.runMeasurement(new Runnable() {
            public void run() {
                measureNormalException();
            }
        });
        Benchmark.Results lightException = runner.runMeasurement(new Runnable() {
            public void run() {
                measureLightException();
            }
        });
        Benchmark.Results normalExceptionMethod = runner.runMeasurement(new Runnable() {
            public void run() {
                measureNormalExceptionMethod();
            }
        });
        Benchmark.Results lightExceptionMethod = runner.runMeasurement(new Runnable() {
            public void run() {
                measureLightExceptionMethod();
            }
        });
        Benchmark.Results createNormalException = runner.runMeasurement(new Runnable() {
            public void run() {
                measureCreateNormalException();
            }
        });
        Benchmark.Results createLightException = runner.runMeasurement(new Runnable() {
            public void run() {
                measureCreateLightException();
            }
        });
        Benchmark.Results mathOperation = runner.runMeasurement(new Runnable() {
            public void run() {
                measureMathOperation();
            }
        });
        Benchmark.Results objectCreation = runner.runMeasurement(new Runnable() {
            public void run() {
                measureObjectCreation();
            }
        });

        System.out.println();
        System.out.println("1: Throw normal exception: " + normalException.getNanos() + " ns");
        System.out.println("2: Throw light exception: " + lightException.getNanos() + " ns");
        System.out.println("3: Throw normal exception + method call: " + normalExceptionMethod.getNanos() + " ns");
        System.out.println("4: Throw light exception + method call: " + lightExceptionMethod.getNanos() + " ns");
        System.out.println("5: Call new Exception(): " + createNormalException.getNanos() + " ns");
        System.out.println("6: Call new LightException(): " + createLightException.getNanos() + " ns");
        System.out.println("7: Call Math.sin(): " + mathOperation.getNanos() + " ns (uses a native method)");
        System.out.println("8: Call new Object(): " + objectCreation.getNanos() + " ns");
        System.out.println();

        double methodOverhead = lightExceptionMethod.getNanos() - lightException.getNanos();
        double exceptionMethodOverhead = normalExceptionMethod.getNanos() - normalException.getNanos() - methodOverhead;
        double lightExceptionSpeedup1 = normalException.getNanos() / lightException.getNanos();
        double lightExceptionSpeedup2 = normalExceptionMethod.getNanos() / lightExceptionMethod.getNanos();
        double throwNormalException = normalException.getNanos() - createNormalException.getNanos();
        double throwLightException = lightException.getNanos() - createLightException.getNanos();
        System.out.println("Method call overhead: " + methodOverhead + " ns");
        System.out.println("Stack trace overhead for a method call: " + exceptionMethodOverhead + " ns");
        System.out.println("Light exceptions compared to normal exceptions: "
                + String.format("%.2f to %.2f", lightExceptionSpeedup1, lightExceptionSpeedup2) + " times faster");
        System.out.println("Throw normal exception (init time subtracted): " + throwNormalException + " ns");
        System.out.println("Throw light exception (init time subtracted): " + throwLightException + " ns");
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
