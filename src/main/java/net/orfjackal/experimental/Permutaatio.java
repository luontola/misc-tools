/*
 * Copyright (c) 2007, Esko Luontola. All Rights Reserved.
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
