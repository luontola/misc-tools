package net.orfjackal.experimental;

import org.objectweb.asm.*;

/**
 * This works on e.g. 32-bit jdk1.6.0_10 (Windows) but not on the 64-bit version or the latest JDK versions.
 * Apparently it was a HotSpot bug.
 */
public class ConstructorSkipping implements Opcodes {

    private static final String CHILD2_CLASS = "$GENERATED$Child2";

    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = new ClassLoader() {
            {
                byte[] child2 = createChild2();
                defineClass(CHILD2_CLASS, child2, 0, child2.length);
            }
        };

        Object child1 = new Child1();
        System.out.println("child1.getClass() = " + child1.getClass());
        System.out.println("child1 instanceof Parent = " + (child1 instanceof Parent));
        System.out.println("---");

        Object child2 = classLoader.loadClass(CHILD2_CLASS).newInstance();
        System.out.println("child2.getClass() = " + child2.getClass());
        System.out.println("child2 instanceof Parent = " + (child2 instanceof Parent));
    }

    public static byte[] createChild2() {
        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;
        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, CHILD2_CLASS, null, Type.getInternalName(Parent.class), null);
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }

    public static class Parent {
        public Parent() {
            // code with unwanted side-effects
            System.out.println("Parent's constructor was called");
        }
    }

    public static class Child1 extends Parent {
        public Child1() {
        }
    }
}
