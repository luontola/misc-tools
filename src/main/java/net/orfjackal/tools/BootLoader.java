package net.orfjackal.tools;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Esko Luontola
 * @since 24.2.2008
 */
public class BootLoader {

    public static final String LIBRARY_DIR = "lib";
    public static final String MAIN_CLASS = "com.foo.Main";

    public static void main(String[] args) throws Exception {
        URL[] libs = toUrls(libraryFiles());
        ClassLoader classLoader = new URLClassLoader(libs, Thread.currentThread().getContextClassLoader());
        start(MAIN_CLASS, args, classLoader);
    }

    private static File[] libraryFiles() {
        File dir = new File(LIBRARY_DIR);
        if (!dir.isDirectory()) {
            throw new IllegalStateException("No such directory: " + dir);
        }
        return dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".jar");
            }
        });
    }

    private static URL[] toUrls(File[] files) throws MalformedURLException {
        URL[] urls = new URL[files.length];
        for (int i = 0; i < files.length; i++) {
            urls[i] = new URL("file", null, files[i].getAbsolutePath());
        }
        return urls;
    }

    private static void start(String mainClass, String[] args, ClassLoader classLoader) throws ClassNotFoundException, NoSuchMethodException, InterruptedException {
        Method main = classLoader.loadClass(mainClass).getMethod("main", String[].class);
        Thread t = new Thread(new MainRunner(main, args), Thread.currentThread().getName());
        t.start();
        t.join(1000);
    }

    private static class MainRunner implements Runnable {

        private final Method main;
        private final String[] args;

        public MainRunner(Method main, String[] args) {
            this.main = main;
            this.args = args;
        }

        public void run() {
            try {
                main.invoke(null, new Object[]{args});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
