package net.orfjackal.experimental;

import org.objectweb.asm.*;
import static org.objectweb.asm.Opcodes.*;

import java.lang.reflect.Field;

/**
 * @author Esko Luontola
 * @since 28.7.2009
 */
public class GeneratedFieldsExperiment {

    public static byte[] generateClassWithFields(int fieldCount) throws Exception {
        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "foo/bar/ClassWithFields", null, "java/lang/Object", null);

        for (int i = 0; i < fieldCount; i++) {
            fv = cw.visitField(ACC_PUBLIC, "var" + i, "Ljava/lang/String;", null, null);
            fv.visitEnd();
        }
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

    public static void main(String[] args) throws Exception {
        MyClassLoader loader = new MyClassLoader();
        Class<?> c = loader.defineClass("foo.bar.ClassWithFields", generateClassWithFields(100));
        System.out.println(c);
        System.out.println("Fields:");
        for (Field field : c.getFields()) {
            System.out.println(field);
        }
    }

    private static class MyClassLoader extends ClassLoader {
        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }
}
