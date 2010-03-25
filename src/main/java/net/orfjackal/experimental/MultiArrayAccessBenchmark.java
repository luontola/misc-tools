package net.orfjackal.experimental;

import java.util.Random;

/**
 * Inspired by http://stackoverflow.com/questions/2512082/java-multi-dimensional-array-vs-one-dimensional
 * <p/>
 * Use for example the following JVM options: -server -Xmx3G -verbose:gc -XX:+PrintCompilation
 *
 * @author Esko Luontola
 * @since 25.3.2010
 */
public class MultiArrayAccessBenchmark {

    private static final int[] DIMENSIONS = {100, 200, 300, 400};
    private static final int REPEATS = 20;
    private static int preventOptimizingAway;

    public static void main(String[] args) {
        System.out.println("Out of " + REPEATS + " repeats, the minimum time in milliseconds is reported.\n");

        for (int dim : DIMENSIONS) {
            System.out.println("Array dimensions: " + dim + "x" + dim + "x" + dim + " (" + (dim * dim * dim) + ")");
            MultiInt3Array multi = new MultiInt3Array(dim, dim, dim);
            SingleInt3Array single = new SingleInt3Array(dim, dim, dim);

            long multiSeqWrite = Long.MAX_VALUE;
            long singleSeqWrite = Long.MAX_VALUE;
            long multiSeqRead = Long.MAX_VALUE;
            long singleSeqRead = Long.MAX_VALUE;
            long multiRandomRead = Long.MAX_VALUE;
            long singleRandomRead = Long.MAX_VALUE;

            for (int repeat = 0; repeat < REPEATS; repeat++) {
                multiSeqWrite = Math.min(multiSeqWrite, sequentialWriteBenchmark(multi, dim));
                singleSeqWrite = Math.min(singleSeqWrite, sequentialWriteBenchmark(single, dim));

                multiSeqRead = Math.min(multiSeqRead, sequentialReadBenchmark(multi, dim));
                singleSeqRead = Math.min(singleSeqRead, sequentialReadBenchmark(single, dim));

                multiRandomRead = Math.min(multiRandomRead, randomReadBenchmark(multi, dim));
                singleRandomRead = Math.min(singleRandomRead, randomReadBenchmark(single, dim));

                System.out.print('.');
            }
            System.out.println();

            System.out.println("        \tMulti\tSingle");
            System.out.println("Seq Write\t" + toMillis(multiSeqWrite) + "\t" + toMillis(singleSeqWrite));
            System.out.println("Seq Read\t" + toMillis(multiSeqRead) + "\t" + toMillis(singleSeqRead));
            System.out.println("Random Read\t" + toMillis(multiRandomRead) + "\t" + toMillis(singleRandomRead));
            System.out.println();
        }
    }

    private static long sequentialWriteBenchmark(Int3Array array, int dim) {
        long start = System.nanoTime();

        int value = 0;
        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                for (int z = 0; z < dim; z++) {
                    value++;
                    array.set(x, y, z, value);
                }
            }
        }

        long end = System.nanoTime();
        return end - start;
    }

    private static long sequentialReadBenchmark(Int3Array array, int dim) {
        long start = System.nanoTime();

        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                for (int z = 0; z < dim; z++) {
                    preventOptimizingAway += array.get(x, y, z);
                }
            }
        }

        long end = System.nanoTime();
        return end - start;
    }

    private static long randomReadBenchmark(Int3Array array, int dim) {
        Random random = new Random(0); // constant seed for determinism
        long start = System.nanoTime();

        int repeats = dim * dim * dim;
        for (int i = 0; i < repeats; i++) {
            int x = random.nextInt(dim);
            int y = random.nextInt(dim);
            int z = random.nextInt(dim);
            preventOptimizingAway += array.get(x, y, z);
        }

        long end = System.nanoTime();
        return end - start;
    }

    private static long toMillis(long nanos) {
        return nanos / 1000000;
    }


    public interface Int3Array {

        int get(int x, int y, int z);

        void set(int x, int y, int z, int value);
    }

    public static class MultiInt3Array implements Int3Array {

        private final int[][][] values;

        public MultiInt3Array(int xDim, int yDim, int zDim) {
            values = new int[xDim][yDim][zDim];
        }

        public int get(int x, int y, int z) {
            return values[x][y][z];
        }

        public void set(int x, int y, int z, int value) {
            values[x][y][z] = value;
        }
    }

    public static class SingleInt3Array implements Int3Array {

        private final int[] values;
        private final int yDim;
        private final int zDim;

        public SingleInt3Array(int xDim, int yDim, int zDim) {
            this.yDim = yDim;
            this.zDim = zDim;
            values = new int[xDim * yDim * zDim];
        }

        public int get(int x, int y, int z) {
            return values[index(x, y, z)];
        }

        public void set(int x, int y, int z, int value) {
            values[index(x, y, z)] = value;
        }

        private int index(int x, int y, int z) {
            return x * yDim * zDim + y * zDim + z;
        }
    }
}
