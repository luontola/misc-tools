package net.orfjackal.experimental;

import org.junit.Test;

import static net.orfjackal.experimental.FindChecksumOfChecksum.*;
import static org.junit.Assert.*;

/**
 * @author Esko Luontola
 * @since 31.3.2010
 */
public class FindChecksumOfChecksumTest {

    @Test
    public void big_endian_has_the_most_significant_byte_first() {
        byte[] actual = intToBytesBigEndian(0x01020304);
        byte[] expected = {0x01, 0x02, 0x03, 0x04};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void little_endian_has_the_least_significant_byte_first() {
        byte[] actual = intToBytesLittleEndian(0x01020304);
        byte[] expected = {0x04, 0x03, 0x02, 0x01};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void hex_representation_is_32_bits_padded_with_0s() {
        assertEquals("00000001", toLowercaseHex32(1));
    }

    @Test
    public void hex_representation_is_by_default_lowercase() {
        assertEquals("ffffffff", toLowercaseHex32(-1));
    }

    @Test
    public void CRC32_is_calculated_correctly() {
        byte[] bytes = "ffffffff".getBytes(ASCII);
        int expected = 0x967E7621; // value got by running QuickSFV on a text file
        assertEquals(expected, getCrc32(bytes));
    }
}
