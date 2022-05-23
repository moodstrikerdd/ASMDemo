package modifyfield;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

public class ModifyFieldClassVisitor extends ClassVisitor {
    public ModifyFieldClassVisitor(int i) {
        super(i);
    }

    public ModifyFieldClassVisitor(int i, ClassVisitor classVisitor) {
        super(i, classVisitor);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if("a".equals(name)){
            return null;
        }
        if("b".equals(name)){
            return super.visitField(access,name,"Ljava/lang/Object;",null,null);
        }
        return super.visitField(access, name, desc, signature, value);
    }
}
