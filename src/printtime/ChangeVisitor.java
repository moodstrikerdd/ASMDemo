package printtime;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

public class ChangeVisitor extends ClassVisitor {
    private String className;

    public ChangeVisitor(int api) {
        super(api);
    }

    public ChangeVisitor(String name, int api, ClassVisitor cv) {
        super(api, cv);
        this.className = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.startsWith("<") || name.startsWith("main")) {
            return methodVisitor;
        } else {
            System.out.println(name);
            return new ChangeAdapter(className, Opcodes.ASM4, methodVisitor, access, name, desc);
        }
    }

    private static class ChangeAdapter extends AdviceAdapter {
        private String className = null;
        private String methodName = null;

        private int state = 0;

        protected ChangeAdapter(String className, int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc);
            this.className = className;
            this.methodName = name;
        }

        private Label start;
        private Label end;
        private int startIndex;

        @Override
        protected void onMethodEnter() {
            super.onMethodEnter();
            start = newLabel();
            end = newLabel();
            startIndex = newLocal(Type.LONG_TYPE);
            visitLabel(start);
            mv.visitLocalVariable("start", "J", null, start, end, startIndex);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitVarInsn(LSTORE, startIndex);
        }

        @Override
        protected void onMethodExit(int opcode) {
            visitLabel(end);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn("class:" + className + "--method:" + methodName + "--times:");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitVarInsn(LLOAD, startIndex);
            mv.visitInsn(LSUB);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
            mv.visitLdcInsn("ms");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            super.onMethodExit(opcode);
        }
    }
}
