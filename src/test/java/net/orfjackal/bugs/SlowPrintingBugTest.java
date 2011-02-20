package net.orfjackal.bugs;

import org.junit.Test;

import java.util.*;
import java.util.logging.Logger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test for reproducing http://youtrack.jetbrains.net/issue/IDEA-62036
 *
 * @author Esko Luontola
 * @since 4.12.2010
 */
public class SlowPrintingBugTest {

    private static Logger logger = Logger.getLogger(SlowPrintingBugTest.class.getName());

    public static final int TOTAL_PRINTS = 1000;
    public static final double PROBABILITY = 0.03;
    public static final int SLOW = 5;

    @Test
    public void printing_should_be_always_fast() throws Exception {
        List<Long> durations = new ArrayList<Long>();
        for (int i = 0; i < TOTAL_PRINTS; i++) {

            // print something to stderr; most of the times this takes 0-1 ms, but every now and then it takes 40-50 ms
            long start = System.currentTimeMillis();
            logger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            long end = System.currentTimeMillis();
            durations.add(end - start);

            // do something for a short moment (otherwise this issue happens less often)
            //Thread.sleep(1);
        }

        int numberOfSlowPrints = 0;
        for (Long duration : durations) {
            if (duration > SLOW) {
                numberOfSlowPrints++;
            }
        }
        System.out.println("times = " + durations);
        System.out.println("numberOfSlowPrints = " + numberOfSlowPrints);
        assertThat("number of slow prints out of " + TOTAL_PRINTS,
                numberOfSlowPrints, is(lessThan((int) (TOTAL_PRINTS * PROBABILITY))));
    }
}
