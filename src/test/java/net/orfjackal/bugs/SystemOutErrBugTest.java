package net.orfjackal.bugs;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Test for reproducing http://youtrack.jetbrains.net/issue/IDEA-62423
 * and http://youtrack.jetbrains.net/issue/IDEA-62424
 *
 * @author Esko Luontola
 * @since 4.12.2010
 */
public class SystemOutErrBugTest {

    @Test
    public void exceptions_stack_traces_should_obey_the_happens_before_relationships() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("<ooooooooooooooooooooooooooooooooo>");
            System.err.println("<eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee>");
        }
        assertEquals(1, 2);
    }
}
