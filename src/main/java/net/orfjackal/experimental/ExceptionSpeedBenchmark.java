/*
 * Copyright (c) 2007, Esko Luontola. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
