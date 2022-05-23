package modifymethod;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class ModifyMethodClassVisitor extends ClassVisitor {
    public ModifyMethodClassVisitor(int i) {
        super(i);
    }

    public ModifyMethodClassVisitor(int i, ClassVisitor classVisitor) {
        super(i, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
        if ("main".equals(name)) {
            methodVisitor = new ModifyMethodVisitor(api, methodVisitor);
        }
        return methodVisitor;
    }

    static class ModifyMethodVisitor extends MethodVisitor {

        public ModifyMethodVisitor(int i) {
            super(i);
        }

        public ModifyMethodVisitor(int i, MethodVisitor methodVisitor) {
            super(i, methodVisitor);
        }

    }
}
