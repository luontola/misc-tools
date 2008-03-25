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
                try {
                    throw new Exception();
                } catch (Exception e) {
                    // NOOP
                }
            }
        });
        Benchmark.Result lightException = benchmark.runBenchmark("Throw light exception", new Runnable() {
            public void run() {
                try {
                    throw new LightException();
                } catch (LightException e) {
                    // NOOP
                }
            }
        });
        Benchmark.Result normalExceptionMethod = benchmark.runBenchmark("Throw normal exception + method call", new Runnable() {
            public void run() {
                throwException();
            }
        });
        Benchmark.Result lightExceptionMethod = benchmark.runBenchmark("Throw light exception + method call", new Runnable() {
            public void run() {
                throwLightException();
            }
        });
        Benchmark.Result createNormalException = benchmark.runBenchmark("Call new Exception()", new Runnable() {
            @SuppressWarnings({"ThrowableInstanceNeverThrown"})
            public void run() {
                new Exception();
            }
        });
        Benchmark.Result createLightException = benchmark.runBenchmark("Call new LightException()", new Runnable() {
            @SuppressWarnings({"ThrowableInstanceNeverThrown"})
            public void run() {
                new LightException();
            }
        });
        Benchmark.Result mathOperation = benchmark.runBenchmark("Call Math.sin() (native method)", new Runnable() {
            public void run() {
                Math.sin(123);
            }
        });
        Benchmark.Result objectCreation = benchmark.runBenchmark("Call new Object()", new Runnable() {
            public void run() {
                new Object();
            }
        });

        benchmark.printResults();

        System.out.println();
        double methodOverhead = lightExceptionMethod.getMedianNanos() - lightException.getMedianNanos();
        double exceptionMethodOverhead = normalExceptionMethod.getMedianNanos() - normalException.getMedianNanos() - methodOverhead;
        double lightExceptionSpeedup1 = normalException.getMedianNanos() / lightException.getMedianNanos();
        double lightExceptionSpeedup2 = normalExceptionMethod.getMedianNanos() / lightExceptionMethod.getMedianNanos();
        double throwNormalException = normalException.getMedianNanos() - createNormalException.getMedianNanos();
        double throwLightException = lightException.getMedianNanos() - createLightException.getMedianNanos();
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
}
