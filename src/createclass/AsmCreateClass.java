package createclass;

import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

public class AsmCreateClass implements Opcodes {
    public static void main(String[] args) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(V1_8,
                ACC_PUBLIC + ACC_FINAL,
                "createclass/ClassCreateByAsm",
                null,
                "java/lang/Object",
                null);
        //field i
        FieldVisitor fieldVisitor = classWriter.visitField(ACC_PRIVATE,
                "i",
                "I",
                null,
                10086);
        fieldVisitor.visitEnd();
        //constructor
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC,
                "<init>",
                "()V",
                null,
                null);
        Label label0 = new Label();
        methodVisitor.visitLabel(label0);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);


        Label label1 = new Label();
        methodVisitor.visitLabel(label1);
        methodVisitor.visitVarInsn(ALOAD,0);
        methodVisitor.visitIntInsn(SIPUSH,10086);
        methodVisitor.visitFieldInsn(PUTFIELD,"createclass/ClassCreateByAsm","i","I");

        methodVisitor.visitInsn(RETURN);

        Label label2 = new Label();
        methodVisitor.visitLabel(label2);
        methodVisitor.visitLocalVariable("this", "Lcreateclass/ClassCreateByAsm;", null, label0, label2, 0);
        methodVisitor.visitMaxs(2, 1);
        methodVisitor.visitEnd();

        //method get i
        MethodVisitor getA = classWriter.visitMethod(ACC_PUBLIC,
                "getA",
                "()I",
                null,
                null);
        getA.visitCode();
        getA.visitVarInsn(ALOAD, 0);
        getA.visitFieldInsn(GETFIELD, "createclass/ClassCreateByAsm", "i", "I");
        getA.visitInsn(IRETURN);
        getA.visitMaxs(1, 1);
        getA.visitEnd();

        classWriter.visitEnd();
        byte[] bytes = classWriter.toByteArray();
        writeByte(bytes);


        try {
            MyClassLoader myClassLoader = new MyClassLoader();
            Class aClass = myClassLoader.defineClass("createclass.ClassCreateByAsm", bytes);
            Object o = aClass.newInstance();
            Method getA1 = aClass.getMethod("getA");
            Object invoke = getA1.invoke(o);
            System.out.println(invoke);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void writeByte(byte[] bytes) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("/Users/moo/workspace/java/ASMDemo/src/createclass/ClassCreateByAsm.class");
            outputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class MyClassLoader extends ClassLoader {
        public Class defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }
}
