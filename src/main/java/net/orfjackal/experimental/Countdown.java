/*
 * Copyright (c) 2007, Esko Luontola. All Rights Reserved.
 */

package net.orfjackal.experimental;

import java.io.IOException;

/**
 * @author Esko Luontola
 * @since 17.5.2007
 */
public class Countdown {

    /**
     * Bytes given by the UNIX command: clear > tmp.txt
     */
    private static final byte[] CLEAR = new byte[]{
            27, 91, 72, 27, 91, 74
    };

    public static void main(String[] args) throws InterruptedException {
        clear();
        for (int i = 10; i >= 1; i--) {
            System.out.println(i);
            Thread.sleep(1000);
            clear();
        }
        System.out.println("*PUM*");
    }

    private static void clear() {
        try {
            System.out.write(CLEAR);
            System.out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
