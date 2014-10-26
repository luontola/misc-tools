package net.orfjackal.experimental;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PrimeNumbersTest {

    @Test
    public void is_prime() {
        assertThat(PrimeNumbers.isPrime(0), is(false));
        assertThat(PrimeNumbers.isPrime(1), is(false));
        assertThat(PrimeNumbers.isPrime(2), is(true));
        assertThat(PrimeNumbers.isPrime(3), is(true));
        assertThat(PrimeNumbers.isPrime(4), is(false));
        assertThat(PrimeNumbers.isPrime(5), is(true));
    }

    @Test
    public void get_primes_up_to() {
        assertThat(PrimeNumbers.getPrimesUpTo(29), is(Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29)));
    }
}