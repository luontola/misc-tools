/*
 * Copyright (c) 2008, Esko Luontola. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
