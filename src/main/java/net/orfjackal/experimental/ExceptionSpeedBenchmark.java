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
        Benchmark benchmark = new Benchmark();

        Benchmark.Result normalException = benchmark.runBenchmark("Throw normal exception", new Runnable() {
            public void run() {
                measureNormalException();
            }
        });
        Benchmark.Result lightException = benchmark.runBenchmark("Throw light exception", new Runnable() {
            public void run() {
                measureLightException();
            }
        });
        Benchmark.Result normalExceptionMethod = benchmark.runBenchmark("Throw normal exception + method call", new Runnable() {
            public void run() {
                measureNormalExceptionMethod();
            }
        });
        Benchmark.Result lightExceptionMethod = benchmark.runBenchmark("Throw light exception + method call", new Runnable() {
            public void run() {
                measureLightExceptionMethod();
            }
        });
        Benchmark.Result createNormalException = benchmark.runBenchmark("Call new Exception()", new Runnable() {
            public void run() {
                measureCreateNormalException();
            }
        });
        Benchmark.Result createLightException = benchmark.runBenchmark("Call new LightException()", new Runnable() {
            public void run() {
                measureCreateLightException();
            }
        });
        Benchmark.Result mathOperation = benchmark.runBenchmark("Call Math.sin() (native method)", new Runnable() {
            public void run() {
                measureMathOperation();
            }
        });
        Benchmark.Result objectCreation = benchmark.runBenchmark("Call new Object()", new Runnable() {
            public void run() {
                measureObjectCreation();
            }
        });

        benchmark.printResults();

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
