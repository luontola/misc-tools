/*
 * Copyright (c) 2008, Esko Luontola. All Rights Reserved.
 */

package net.orfjackal.tools;

/**
 * @author Esko Luontola
 * @since 30.1.2008
 */
public class BenchmarkRunner {

    private final int warmupRounds;

    public BenchmarkRunner() {
        this(3);
    }

    public BenchmarkRunner(int warmupRounds) {
        this.warmupRounds = warmupRounds;
    }

    public long runMeasurement(int repeats, Runnable benchmark) {
        for (int i = 0; i < warmupRounds; i++) {
            repeat(repeats, benchmark);
        }
        long start = System.currentTimeMillis();
        repeat(repeats, benchmark);
        long end = System.currentTimeMillis();
        return end - start;
    }

    private static void repeat(int repeats, Runnable benchmark) {
        for (int i = 0; i < repeats; i++) {
            benchmark.run();
        }
    }
}
