/*
 * Copyright (c) 2008, Esko Luontola. All Rights Reserved.
 */

package net.orfjackal.tools;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Esko Luontola
 * @since 30.1.2008
 */
public class Benchmark {

    private final int warmupRounds;
    private final int minimumDurationMs;
    private final List<Result> results = new ArrayList<Result>();

    public Benchmark() {
        this(3, 500);
    }

    public Benchmark(int warmupRounds, int minimumDurationMs) {
        this.warmupRounds = warmupRounds;
        this.minimumDurationMs = minimumDurationMs;
    }

    public Result runBenchmark(String description, Runnable benchmark) {
        int repeats = autoRepeatCount(benchmark);
        long durationMs = finalMeasurement(benchmark, repeats);
        Result result = new Result(description, repeats, durationMs);
        results.add(result);
        return result;
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

    public void printResults() {
        DecimalFormat nf = getNumberFormat();
        int descMaxLength = 0;
        int nanosMaxLength = 0;
        for (Result result : results) {
            descMaxLength = Math.max(descMaxLength, result.getDescription().length());
            nanosMaxLength = Math.max(nanosMaxLength, nf.format(result.getNanos()).length());
        }
        System.out.println("Benchmark Results");
        for (int i = 0; i < results.size(); i++) {
            Result result = results.get(i);
            System.out.println((i + 1) + ": " + pad(result.getDescription(), descMaxLength, " ")
                    + "    " + pad(nf.format(result.getNanos()), -nanosMaxLength, " ") + " ns");
        }
    }

    private static DecimalFormat getNumberFormat() {
        return new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
    }

    private static String pad(Object str, int padlen, String pad) {
        String padding = "";
        int len = Math.abs(padlen) - str.toString().length();
        if (len < 1) {
            return str.toString();
        }
        for (int i = 0; i < len; ++i) {
            padding = padding + pad;
        }
        return (padlen < 0 ? padding + str : str + padding);
    }

    public static class Result {

        private static final double MILLIS_TO_NANOS = 1000 * 1000;

        private final String description;

        private final int repeats;

        private final long totalDurationMs;

        public Result(String description, int repeats, long totalDurationMs) {
            this.description = description;
            this.repeats = repeats;
            this.totalDurationMs = totalDurationMs;
        }

        public String getDescription() {
            return description;
        }

        public int getRepeats() {
            return repeats;
        }

        public long getTotalMillis() {
            return totalDurationMs;
        }

        public double getNanos() {
            return totalDurationMs * (MILLIS_TO_NANOS / repeats);
        }

        public String toString() {
            NumberFormat nf = getNumberFormat();
            return "Benchmark.Result[" + getDescription() + ": " + nf.format(getNanos()) + " ns ("
                    + getRepeats() + " repeats, " + getTotalMillis() + " ms)]";
        }

    }
}
