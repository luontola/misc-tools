package net.orfjackal.experimental;

import org.objectweb.asm.*;

/**
 * Experiment about using bytecode to constructor inject cyclic dependencies.
 * Result: It cannot be done, because the JVM bytecode verifier does not allow
 * one to pass object instances to a method or field, until the constructor of
 * that instance has been called. Also calling a constructor twice is not allowed.
 *
 * @author Esko Luontola
 * @since 5.2.2010
 */
public class ConstructorInjectingCircularDependencies implements Opcodes {

    public static void main(String[] args) throws Exception {
        String classFactory = Type.getInternalName(Factory.class);
        String classGeneratedFactory = "GeneratedFactory";
        String classA = Type.getInternalName(A.class);
        String classB = Type.getInternalName(B.class);

        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_6, ACC_PUBLIC, classGeneratedFactory, null, classFactory, null);
        MethodVisitor mv;
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, classFactory, "<init>", "()V");
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "createAB", "()V", null, null);
            mv.visitCode();

            // var1 = new A
            mv.visitVarInsn(ALOAD, 0);
            mv.visitTypeInsn(NEW, classA);
            mv.visitVarInsn(ASTORE, 1);

            // var2 = new B
            mv.visitVarInsn(ALOAD, 0);
            mv.visitTypeInsn(NEW, classB);
            mv.visitVarInsn(ASTORE, 2);

            // var1.A(var2)
            mv.visitVarInsn(ALOAD, 1);
            // An object cannot be passed to another method before calling its constructor
            //     java.lang.VerifyError: Expecting to find object/array on stack
//            mv.visitVarInsn(ALOAD, 2);
            mv.visitInsn(ACONST_NULL); // can only use null here
            mv.visitMethodInsn(INVOKESPECIAL, classA, "<init>", "(L" + classB + ";)V");

            // var2.A(var1)
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, classB, "<init>", "(L" + classA + ";)V");

            // The constructor cannot be called twice:
            //     java.lang.VerifyError: Expecting to find unitialized object on stack
//            mv.visitVarInsn(ALOAD, 1);
//            mv.visitVarInsn(ALOAD, 2);
//            mv.visitMethodInsn(INVOKESPECIAL, classA, "<init>", "(L" + classB + ";)V");

            // this.a = var1
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, classFactory, "a", "L" + classA + ";");

            // this.b = var2
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitFieldInsn(PUTFIELD, classFactory, "b", "L" + classB + ";");

            mv.visitInsn(RETURN);
            mv.visitMaxs(10, 3);
            mv.visitEnd();
        }
        cw.visitEnd();
        byte[] bytes = cw.toByteArray();

        CustomClassLoader cl = new CustomClassLoader();
        cl.defineClass(classGeneratedFactory, bytes);
        Class<?> factoryClass = cl.loadClass(classGeneratedFactory);

        Factory factory = (Factory) factoryClass.newInstance();
        factory.createAB();
        System.out.println("a:   " + factory.a);
        System.out.println("b:   " + factory.b);
        System.out.println("a.b: " + factory.a.b);
        System.out.println("b.a: " + factory.b.a);
        System.out.println("b == a.b: " + (factory.b == factory.a.b));
        System.out.println("a == b.a: " + (factory.a == factory.b.a));
    }

    public static class A {
        public B b;

        public A(B b) {
            this.b = b;
        }
    }

    public static class B {
        public A a;

        public B(A a) {
            this.a = a;
        }
    }

    public static abstract class Factory {
        public A a;
        public B b;

        public abstract void createAB();
    }

    public static class CustomClassLoader extends ClassLoader {

        public void defineClass(String name, byte[] bytes) {
            defineClass(name, bytes, 0, bytes.length);
        }
    }
}
