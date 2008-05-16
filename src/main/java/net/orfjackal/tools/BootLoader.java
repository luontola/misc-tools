/*
 * Copyright (c) 2008, Esko Luontola. All Rights Reserved.
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

package net.orfjackal.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

/**
 * Starts up a program by dynamically loading all JAR libraries in one directory. The library directory
 * and main class can be defined in "{@value #PROPERTIES_PATH}" using keys "{@value #LIBRARY_DIR_KEY}"
 * and "{@value #MAIN_CLASS_KEY}". This bootloader class and the properties file should be put into an
 * executable JAR file whose manifest's Main-Class is "{@code net.orfjackal.tools.BootLoader}". All other
 * classes should be as JARs in the library directory.
 *
 * @author Esko Luontola
 * @since 24.2.2008
 */
public class BootLoader {

    // Inspired by http://www.jroller.com/ssourcery/entry/get_rid_of_the_classpath

    public static final String PROPERTIES_PATH = "/bootloader.properties";
    public static final String LIBRARY_DIR_KEY = "libraryDir";
    public static final String MAIN_CLASS_KEY = "mainClass";

    private static String libraryDir;
    private static String mainClass;

    public static void main(String[] args) throws Exception {
        readProperties();
        URL[] libs = asUrls(listJarsIn(new File(libraryDir)));
        ClassLoader classLoader = new URLClassLoader(libs, Thread.currentThread().getContextClassLoader());
        start(mainClass, args, classLoader);
    }

    private static void readProperties() throws IOException {
        Properties p = new Properties();
        p.load(BootLoader.class.getResourceAsStream(PROPERTIES_PATH));
        libraryDir = getRequiredProperty(p, LIBRARY_DIR_KEY);
        mainClass = getRequiredProperty(p, MAIN_CLASS_KEY);
    }

    private static String getRequiredProperty(Properties p, String key) {
        String value = p.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException(
                    "Required key \"" + key + "\" not found from file \"" + PROPERTIES_PATH + "\"");
        }
        return value;
    }

    private static File[] listJarsIn(File dir) {
        if (!dir.isDirectory()) {
            throw new IllegalStateException("No such directory: " + dir);
        }
        return dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".jar");
            }
        });
    }

    private static URL[] asUrls(File[] files) throws MalformedURLException {
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
