package net.orfjackal.experimental;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.management.ManagementFactory;

/**
 * Proving that the instances of JUnit test classes can be GC'd right after the test
 * has been executed. Already from {@link org.junit.runners.BlockJUnit4ClassRunner#runChild}
 * it can be seen that JUnit doesn't keep hold of the instances, and this test proves it
 * when run with {@code -Xmx160M} or similar heap limit.
 *
 * @author Esko Luontola
 * @since 13.3.2012
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class JUnitMemoryTest {

    private static final int MB = 1024 * 1024;

    private byte[] allocated = allocate(100 * MB);

    private static byte[] allocate(int size) {
        while (true) {
            System.gc();
            try {
                return new byte[size];
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
    }

    public JUnitMemoryTest() {
        System.out.println(this + " created");
    }

    protected void finalize() throws Throwable {
        System.out.println(this + " finalized");
    }

    private void printStats() {
        System.out.println(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage());
    }

    @Test
    public void test1() {
        printStats();
    }

    @Test
    public void test2() {
        printStats();
    }

    @Test
    public void test3() {
        printStats();
    }

    @Test
    public void test4() {
        printStats();
    }

    @Test
    public void test5() {
        printStats();
    }

    @Test
    public void test6() {
        printStats();
    }

    @Test
    public void test7() {
        printStats();
    }

    @Test
    public void test8() {
        printStats();
    }

    @Test
    public void test9() {
        printStats();
    }

    @Test
    public void test10() {
        printStats();
    }
}
