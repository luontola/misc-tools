/*
 * Copyright (c) 2008, Esko Luontola. All Rights Reserved.
 */

package net.orfjackal.tools;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Esko Luontola
 * @since 30.1.2008
 */
public class BenchmarkRunner {

    private final int warmupRounds;
    private final int minimumDurationMs;

    public BenchmarkRunner() {
        this(3, 500);
    }

    public BenchmarkRunner(int warmupRounds, int minimumDurationMs) {
        this.warmupRounds = warmupRounds;
        this.minimumDurationMs = minimumDurationMs;
    }

    public long runMeasurement(Runnable benchmark) {
        int repeats = autoRepeatCount(benchmark);
        long durationMs = finalMeasurement(benchmark, repeats);
        System.out.println(new Results(repeats, durationMs)); // TODO: return Result
        return durationMs;
    }

    private int autoRepeatCount(Runnable benchmark) {
        int repeats = 1;
        long durationMs = 0;
        while (durationMs < minimumDurationMs) {
            long start = System.currentTimeMillis();
            repeat(repeats, benchmark);
            long end = System.currentTimeMillis();

            durationMs = end - start;
            if (durationMs < minimumDurationMs) {
                repeats = repeats * 2;
            }
        }
        return repeats;
    }

    private long finalMeasurement(Runnable benchmark, int repeats) {
        long start = 0;
        long end = 0;
        for (int i = 0; i < warmupRounds; i++) {
            start = System.currentTimeMillis();
            repeat(repeats, benchmark);
            end = System.currentTimeMillis();
        }
        return end - start;
    }

    private static void repeat(int repeats, Runnable benchmark) {
        for (int i = 0; i < repeats; i++) {
            benchmark.run();
        }
    }

    public static class Results {

        private static final double MILLIS_TO_NANOS = 1000 * 1000;

        private final int repeats;
        private final long totalDurationMs;

        public Results(int repeats, long totalDurationMs) {
            this.repeats = repeats;
            this.totalDurationMs = totalDurationMs;
        }

        public int getRepeats() {
            return repeats;
        }

        public long getTotalDurationMillis() {
            return totalDurationMs;
        }

        public double getOperationDurationNanos() {
            return totalDurationMs * (MILLIS_TO_NANOS / repeats);
        }

        public String toString() {
            NumberFormat nf = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
            return nf.format(getOperationDurationNanos()) + " ns ("
                    + getRepeats() + " repeats, " + getTotalDurationMillis() + " ms)";
        }
    }
}
