/*
 * Copyright (c) 2008, Esko Luontola. All Rights Reserved.
 */

package net.orfjackal.experimental.nineplaces;

import java.util.ArrayList;
import java.util.List;

/**
 * Diagram of Nine Places - Put the numbers 1 to 9 into the squares so it adds to 15
 * whether you view it horizontally, vertically or diagonally.
 * <pre>
 *   _ _ _
 *  |_|_|_|
 *  |_|_|_|
 *  |_|_|_|
 * </pre>
 * - Legend of the Condor Heroes (1982), episode 44
 * <p/>
 * Also see
 * http://www.magicl-existence.com/
 * http://www.fengshuigate.com/numerology.html
 * http://catenary.wordpress.com/2006/08/19/fun-with-representations-i-nine-numbers/
 *
 * @author Esko Luontola
 * @since 8.4.2008
 */
public class DiagramOfNinePlaces {

    private static List<Diagram> solutions = new ArrayList<Diagram>();
    private static int tries = 0;

    public static void main(String[] args) {
        Diagram diagram = new Diagram();
        search(diagram, 0);
        for (Diagram solution : solutions) {
            System.out.println(solution);
        }
        System.out.println("Total tries: " + tries);
    }

    private static void search(Diagram diagram, int nextIndex) {
        if (nextIndex < diagram.length()) {
            for (int value = 1; value <= 9; value++) {
                Diagram assigned = diagram.with(nextIndex, value);
                if (assigned != null) {
                    search(assigned, nextIndex + 1);
                }
                tries++;
            }
        } else {
            assert diagram.full();
            if (!diagram.fail()) {
                solutions.add(diagram);
            }
        }
    }
}
