package printmethod.custom;

import org.objectweb.asm.*;

public class PrintClassVisitor extends ClassVisitor {
    public PrintClassVisitor(int api) {
        super(api);
    }

    public PrintClassVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//        System.out.println("PrintClassVisitor - visit -> version : " + version + " access : " + access + " name : " + name + " signature : " + signature + " superName : " + superName + " interfaces : " + Arrays.toString(interfaces));
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitSource(String source, String debug) {
//        System.out.println("PrintClassVisitor - visitSource -> source : " + source + " debug : " + debug);
        super.visitSource(source, debug);
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
//        System.out.println("PrintClassVisitor - visitOuterClass -> owner : " + owner + " name : " + name + " desc : " + desc);
        super.visitOuterClass(owner, name, desc);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
//        System.out.println("PrintClassVisitor - visitAnnotation -> desc : " + desc + " visible : " + visible);
        AnnotationVisitor annotationVisitor = super.visitAnnotation(desc, visible);
        return new PrintAnnotationVisitor(api, annotationVisitor);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitTypeAnnotation(typeRef, typePath, desc, visible);
        return new PrintAnnotationVisitor(api, annotationVisitor);
    }

    @Override
    public void visitAttribute(Attribute attr) {
        super.visitAttribute(attr);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        FieldVisitor fieldVisitor = super.visitField(access, name, "J", signature, value);
        return new PrintFieldVisitor(api, fieldVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
        return new PrintMethodVisitor(api, methodVisitor);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
