package net.orfjackal.experimental;

import junit.framework.TestCase;


public class FizzBuzzTest extends TestCase {

    public void test__Multiples_of_3_print_Fizz() {
        assertEquals("Fizz", FizzBuzz.textForNumber(3));
        assertEquals("Fizz", FizzBuzz.textForNumber(6));
    }

    public void test__Multiples_of_5_print_Buzz() {
        assertEquals("Buzz", FizzBuzz.textForNumber(5));
        assertEquals("Buzz", FizzBuzz.textForNumber(10));
    }

    public void test__Multiples_of_both_3_and_5_print_FizzBuzz() {
        assertEquals("FizzBuzz", FizzBuzz.textForNumber(15));
        assertEquals("FizzBuzz", FizzBuzz.textForNumber(30));
    }

    public void test__All_others_print_the_number() {
        assertEquals("1", FizzBuzz.textForNumber(1));
        assertEquals("2", FizzBuzz.textForNumber(2));
        assertEquals("4", FizzBuzz.textForNumber(4));
    }
}
