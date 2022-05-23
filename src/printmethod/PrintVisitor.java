package printmethod;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_NATIVE;

public class PrintVisitor extends ClassVisitor {
    private String className = null;

    public PrintVisitor(String name, int api) {
        super(api);
        className = name;
    }

    public PrintVisitor(String name, int api, ClassVisitor cv) {
        super(api, cv);
        className = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (methodVisitor != null && !name.equals("<init>")) {
            boolean isAbstractMethod = (access & ACC_ABSTRACT) != 0;
            boolean isNativeMethod = (access & ACC_NATIVE) != 0;
            if (!isAbstractMethod && !isNativeMethod) {
                methodVisitor = new PrintMethodAdapter(className, name, api, methodVisitor, access, name, desc);
            }
        }
        return methodVisitor;
    }

    private static class PrintMethodAdapter extends AdviceAdapter {
        private String className = null;
        private String methodName = null;

        protected PrintMethodAdapter(String className, String methodName, int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc);
            this.className = className;
            this.methodName = methodName;
        }

        @Override
        protected void onMethodEnter() {
            boolean isStatic = ((methodAccess & ACC_STATIC) != 0);
            int slotIndex = isStatic ? 0 : 1;
            printMessage(className + "->" + methodName + " ");

            Type methodType = Type.getMethodType(methodDesc);
            Type[] argumentTypes = methodType.getArgumentTypes();
            for (Type t : argumentTypes) {
                int sort = t.getSort();
                int size = t.getSize();
                String descriptor = t.getDescriptor();
                String signature = "";
                if (sort == Type.BOOLEAN) {
                    signature = "(Z)V";
                } else if (sort == Type.CHAR) {
                    signature = "(C)V";
                } else if (sort == Type.BYTE || sort == Type.SHORT || sort == Type.INT) {
                    signature = "(I)V";
                } else if (sort == Type.FLOAT) {
                    signature = "(F)V";
                } else if (sort == Type.LONG) {
                    signature = "(J)V";
                } else if (sort == Type.DOUBLE) {
                    signature = "(D)V";
                } else if (sort == Type.OBJECT && "Ljava/lang/String;".equals(descriptor)) {
                    signature = "(Ljava/lang/String;)V";
                } else if (sort == Type.OBJECT) {
                    signature = "(Ljava/lang/Object;)V";
                } else if (sort == Type.ARRAY) {
                    printArray(t, slotIndex);
                } else {
                    printMessage("No Support");
                }
                if (!"".equals(signature)) {
                    super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    int opcode = t.getOpcode(ILOAD);
                    super.visitVarInsn(opcode, slotIndex);
                    super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", signature, false);
                }
                slotIndex += size;
                printSpace();
            }
            println();
        }

        private void printString() {
            super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitInsn(SWAP);
            super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
        }

        private void printObject() {
            super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitInsn(SWAP);
            super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/Object;)V", false);
        }

        private void printMessage(String str) {
            super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitLdcInsn(str);
            super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
        }

        private void printSpace() {
            super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitLdcInsn(" ");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
        }

        private void println() {
            super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "()V", false);
        }

        private void printArray(Type t, int slotIndex) {
            String signature = t.getDescriptor();
            if (signature.length() > 2) {
                signature = "[Ljava/lang/Object;";
            }
            int opcode = t.getOpcode(ILOAD);
            super.visitVarInsn(opcode, slotIndex);
            String toString = "(" + signature + ")Ljava/lang/String;";
            super.visitMethodInsn(INVOKESTATIC, "java/util/Arrays", "toString", toString, false);
            printString();
        }

    }
}
