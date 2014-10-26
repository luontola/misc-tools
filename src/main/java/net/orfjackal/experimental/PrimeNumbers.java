package net.orfjackal.experimental;

import java.util.*;

/**
 * See http://www.mkyong.com/java/how-to-determine-a-prime-number-in-java/
 */
public class PrimeNumbers {

    private static boolean[] primes = new boolean[]{false, false};

    public static boolean isPrime(int n) {
        if (!(n < primes.length)) {
            sieve(n);
        }
        return primes[n];
    }

    private static void sieve(int limit) {
        primes = new boolean[limit + 1];
        Arrays.fill(primes, true);
        primes[0] = false;
        primes[1] = false;

        for (int n = 0; n < primes.length; n++) {
            if (primes[n]) {
                crossOutMultiples(n);
            }
        }
    }

    private static void crossOutMultiples(int prime) {
        for (int n = prime + prime; n < primes.length; n += prime) {
            primes[n] = false;
        }
    }

    public static List<Integer> getPrimesUpTo(int limit) {
        List<Integer> results = new ArrayList<>();
        for (int n = 0; n <= limit; n++) {
            if (isPrime(n)) {
                results.add(n);
            }
        }
        return results;
    }
}
