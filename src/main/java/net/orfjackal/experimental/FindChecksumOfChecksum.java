package net.orfjackal.experimental;

import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.CRC32;

/**
 * Inspired by http://stackoverflow.com/questions/2554143/how-to-find-a-checksum-of-the-same-checksum-job-interview-question
 *
 * @author Esko Luontola
 * @since 31.3.2010
 */
public class FindChecksumOfChecksum {

    private static final Charset ASCII = Charset.forName("US-ASCII");

    public static void main(String[] args) {
        System.out.println("Start on " + new Date());

        for (long i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            if (i % (Integer.MAX_VALUE / 128) == 0) {
                System.out.println("Calculating... " + i);
            }
            checkChecksumOfChecksum((int) i);
        }

        System.out.println("End on " + new Date());
    }

    private static void checkChecksumOfChecksum(int checksum) {
        String hex = toHex32(checksum);
        byte[] lowercase = hex.getBytes(ASCII);
        byte[] uppercase = hex.toUpperCase().getBytes(ASCII);
        byte[] bigEndian = intToBytesBigEndian(checksum);
        byte[] littleEndian = intToBytesLittleEndian(checksum);

        checkChecksum(checksum, lowercase);
        checkChecksum(checksum, uppercase);
        checkChecksum(checksum, bigEndian);
        checkChecksum(checksum, littleEndian);
    }

    private static String toHex32(int checksum) {
        String hex = Integer.toHexString(checksum);
        while (hex.length() < 8) {
            hex = "0" + hex;
        }
        return hex;
    }

    private static byte[] intToBytesBigEndian(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value,
        };
    }

    private static byte[] intToBytesLittleEndian(int value) {
        return new byte[]{
                (byte) value,
                (byte) (value >>> 8),
                (byte) (value >>> 16),
                (byte) (value >>> 24),
        };
    }

    private static void checkChecksum(int expected, byte[] bytes) {
        if (getCrc32(bytes) == expected) {
            System.out.println("FOUND! " + toHex32(expected) + " " + Arrays.toString(bytes));
        }
    }

    private static int getCrc32(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return (int) crc.getValue();
    }
}
