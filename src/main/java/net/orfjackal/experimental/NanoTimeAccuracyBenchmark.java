/*
 * Copyright (c) 2007, Esko Luontola. All Rights Reserved.
 */

package net.orfjackal.experimental;

public class NanoTimeAccuracyBenchmark {
    public static void main(String[] args) {
        long time1;
        long time2;

        System.out.println("System.currentTimeMillis()");
        for (int i = 0; i < 20; i++) {
            int j = 0;
            time1 = System.currentTimeMillis();
            while ((time2 = System.currentTimeMillis()) == time1) {
                j++;
            }
            System.out.println((time2 - time1) + " ms (" + j + ")");
        }

        System.out.println();
        System.out.println("System.nanoTime()");
        for (int i = 0; i < 50; i++) {
            int j = 0;
            time1 = System.nanoTime();
            while ((time2 = System.nanoTime()) == time1) {
                j++;
            }
            System.out.println((time2 - time1) + " ns (" + j + ")");
        }
    }
}
