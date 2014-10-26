package net.orfjackal.experimental;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Esko Luontola
 * @since 25.3.2010
 */
public class MultiArrayAccessBenchmarkTest {

    private static final int X_DIM = 10;
    private static final int Y_DIM = 20;
    private static final int Z_DIM = 30;

    private MultiArrayAccessBenchmark.MultiInt3Array multiArray = new MultiArrayAccessBenchmark.MultiInt3Array(X_DIM, Y_DIM, Z_DIM);
    private MultiArrayAccessBenchmark.SingleInt3Array singleArray = new MultiArrayAccessBenchmark.SingleInt3Array(X_DIM, Y_DIM, Z_DIM);

    @Test
    public void multi_array_first_and_last_values() {
        multiArray.set(0, 0, 0, 111);
        multiArray.set(9, 19, 29, 999);

        assertEquals(111, multiArray.get(0, 0, 0));
        assertEquals(999, multiArray.get(9, 19, 29));
    }

    @Test
    public void single_array_first_and_last_values() {
        singleArray.set(0, 0, 0, 111);
        singleArray.set(9, 19, 29, 999);

        assertEquals(111, singleArray.get(0, 0, 0));
        assertEquals(999, singleArray.get(9, 19, 29));
    }

    @Test
    public void all_values_are_saved_to_their_own_slots() {
        int value = 0;
        for (int x = 0; x < X_DIM; x++) {
            for (int y = 0; y < Y_DIM; y++) {
                for (int z = 0; z < Z_DIM; z++) {
                    value++;
                    multiArray.set(x, y, z, value);
                    singleArray.set(x, y, z, value);
                }
            }
        }
        value = 0;
        for (int x = 0; x < X_DIM; x++) {
            for (int y = 0; y < Y_DIM; y++) {
                for (int z = 0; z < Z_DIM; z++) {
                    value++;
                    String message = x + "," + y + "," + z;
                    assertEquals(message, value, multiArray.get(x, y, z));
                    assertEquals(message, value, singleArray.get(x, y, z));
                }
            }
        }
    }
}
