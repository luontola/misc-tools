/*
 * Copyright (c) 2007, Esko Luontola. All Rights Reserved.
 */

package net.orfjackal.experimental;

/**
 * @author Esko Luontola
 * @since 12.2.2007
 */
public class CharacterCodePrinter {
    public static void main(String[] args) {
        System.out.println((char) 32);

        for (int i = 32; i < 10000; i++) {
            System.out.println(i + " = " + (char) i);
        }
    }
}
