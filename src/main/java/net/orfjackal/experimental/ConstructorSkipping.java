package net.orfjackal.experimental;

import org.objectweb.asm.*;

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
        Object child2 = classLoader.loadClass(CHILD2_CLASS).newInstance();
        Object parent = new Parent();
        System.out.println("child1 = " + child1);
        System.out.println("child2 = " + child2);
        System.out.println("parent = " + parent);
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
            System.out.println("Parent's constructor was called");
            System.out.println("getClass() = " + getClass());
            System.out.println("--");
        }
    }

    public static class Child1 extends Parent {
        public Child1() {
        }
    }
}