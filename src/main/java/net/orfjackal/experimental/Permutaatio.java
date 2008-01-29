/*
 * Copyright (c) 2007, Esko Luontola. All Rights Reserved.
 */

package net.orfjackal.experimental;

import java.util.Scanner;

/**
 * Tehtävä: Tee ohjelma, joka lukee päätteeltä seitsemän eri kirjainta ja laskee niiden permutaatiot ja tulostaa joka
 * sadannen näytölle.
 * <p/>
 * Permutaatio voitaisiin arkikielessä määritellä seuraavasti: Kuinka moneen eri järjestykseen tietyt merkit voidaan
 * järjestää. Kaikki eri järjestykset voidaan laskea kertoman avulla, esim kirjainsarjassa abc on 3 eri merkkiä joten
 * eri kombinaatioita on 3! (3:n kertoma) = 1*2*3 = 6. Näinollen 7:n kirjaimen tapauksessa permutaatioita on 7! =
 * 1*2*3*4*5*6*7 = 5040kpl.
 */
public class Permutaatio {

    public static final int CHAR_COUNT = 7;

    private static char[] chars = new char[CHAR_COUNT];
    private static boolean[] charUsed = new boolean[CHAR_COUNT];
    private static char[] perm = new char[CHAR_COUNT];
    private static int permCount = 0;

    public static void main(String[] args) {
        System.out.println("Anna " + chars.length + " eri kirjainta (erottele rivinvaihdolla):");
        Scanner in = new Scanner(System.in);
        for (int i = 0; i < chars.length; i++) {
            chars[i] = in.next().charAt(0);
            for (int j = 0; j < i; j++) {
                if (chars[i] == chars[j]) {
                    i--;
                    break;
                }
            }
        }
        long time = System.nanoTime();
        doPerm(0);
        time = System.nanoTime() - time;
        System.out.println("Yhteensä " + permCount + " permutaatiota. (" + time / 1000000.0 + " ms)");
    }

    private static void doPerm(int permIndex) {
        if (permIndex >= perm.length) {
            permCount++;
            if (perm.length < 6 || permCount % 100 == 0) {
                System.out.println(perm);
            }
            return;
        }
        for (int i = 0; i < chars.length; i++) {
            if (charUsed[i]) {
                continue;
            }
            charUsed[i] = true;
            perm[permIndex] = chars[i];
            doPerm(permIndex + 1);
            charUsed[i] = false;
        }
    }
}
