package printmethod.custom;

import org.objectweb.asm.AnnotationVisitor;

public class PrintAnnotationVisitor extends AnnotationVisitor {
    public PrintAnnotationVisitor(int api) {
        super(api);
    }

    public PrintAnnotationVisitor(int api, AnnotationVisitor av) {
        super(api, av);
    }

    @Override
    public void visit(String name, Object value) {
//        System.out.println("PrintAnnotationVisitor - visit -> name : " + name + " value : " + value);
        super.visit(name, value);
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
//        System.out.println("PrintAnnotationVisitor - visitEnum -> name : " + name + " desc : " + desc + " value : " + value);
        super.visitEnum(name, desc, value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String desc) {
//        System.out.println("PrintAnnotationVisitor - visitAnnotation -> name : " + name + " desc : " + desc);
        return super.visitAnnotation(name, desc);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
//        System.out.println("PrintAnnotationVisitor - visitArray -> name : " + name);
        return super.visitArray(name);
    }

    @Override
    public void visitEnd() {
//        System.out.println("PrintAnnotationVisitor - visitEnd");
        super.visitEnd();
    }
}
