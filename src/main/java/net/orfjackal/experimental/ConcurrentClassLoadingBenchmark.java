package net.orfjackal.experimental;

import java.io.File;
import java.util.Enumeration;
import java.util.concurrent.*;
import java.util.jar.*;

/**
 * Test whether loading classes from multiple threads speeds it up. JDK 7 has {@link ClassLoader#registerAsParallelCapable}
 * and supports parallel capable class loaders.
 * <p/>
 * Results on C2Q6600 (4 cores)<br>
 * - JDK 6's class loading is single-threaded, and trying to load in parallel causes ~10% slowdown<br>
 * - JDK 7's class loading can be parallelized, and loading in parallel brings ~40% speedup
 *
 * @author Esko Luontola
 * @since 26.7.2010
 */
public class ConcurrentClassLoadingBenchmark {
    private static final int N_THREADS = Runtime.getRuntime().availableProcessors();
//    private static final int N_THREADS = 1;

    private static final ClassLoader classLoader = ConcurrentClassLoadingBenchmark.class.getClassLoader();
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(N_THREADS);
    private static int classCount = 0;

    public static void main(String[] args) throws Exception {
        File bigJarFile = toFile(args[0]);

        long start = System.nanoTime();
        loadAllClassesIn(bigJarFile);
        long end = System.nanoTime();

        double duration = nanosToMillis(end - start);
        System.out.println("It took " + duration + " ms to load all the " + classCount + " classes in " + bigJarFile
                + " using " + N_THREADS + " threads");
    }

    private static File toFile(String s) {
        File file = new File(s);
        if (!file.isFile()) {
            throw new IllegalArgumentException("Not a file: " + file);
        }
        return file;
    }

    private static double nanosToMillis(long nanos) {
        return nanos / 1000000.0;
    }

    private static void loadAllClassesIn(File jarFile) throws Exception {
        JarFile jar = new JarFile(jarFile);
        try {
            loadAllClassesIn(jar);
        } finally {
            jar.close();
        }
    }

    private static void loadAllClassesIn(JarFile jarFile) throws InterruptedException {
        Enumeration<JarEntry> it = jarFile.entries();
        while (it.hasMoreElements()) {
            JarEntry entry = it.nextElement();
            if (isClass(entry)) {
                queueForLoadingInParallel(getClassName(entry));
            }
        }
        waitForClassesToLoad();
    }

    private static boolean isClass(JarEntry entry) {
        return entry.getName().endsWith(".class");
    }

    private static String getClassName(JarEntry entry) {
        String name = entry.getName();
        return name.substring(0, name.lastIndexOf(".class")).replaceAll("/", ".");
    }

    private static void queueForLoadingInParallel(final String className) {
        classCount++;
        threadPool.submit(new Runnable() {
            public void run() {
                try {
                    classLoader.loadClass(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void waitForClassesToLoad() throws InterruptedException {
        threadPool.shutdown();
        threadPool.awaitTermination(100, TimeUnit.SECONDS);
    }
}
