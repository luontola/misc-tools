/*
 * Copyright (c) 2007, Esko Luontola. All Rights Reserved.
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

package net.orfjackal.experimental;

public class AccessingAnUninitializedClass {

    public static Object obj;

    public static class Foo {
        private String foo;

        public Foo(String s) throws Exception {
            foo = s;
            obj = this;
            throw new Exception();
        }

        public String getFoo() {
            return foo;
        }
    }

    public static class Bar extends Foo {
        private String bar;

        public Bar(String s) throws Exception {
            super(s);
            bar = s;
        }

        public String getBar() {
            return bar;
        }
    }

    public static void main(String[] args) {
        Bar b = null;
        System.out.println("obj: " + obj);
        try {
            b = new Bar("Foobar");
        } catch (Exception e) {
            // NOOP
        }
        System.out.println("  b: " + b);
        System.out.println("obj: " + obj);
        System.out.println("Foo: " + ((Foo) obj).getFoo());
        System.out.println("Bar: " + ((Bar) obj).getBar());
    }
}
