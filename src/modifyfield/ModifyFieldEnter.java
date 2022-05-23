package modifyfield;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;

public class ModifyFieldEnter implements Opcodes {

    public static void main(String[] args) {
        String path = "/Users/moo/workspace/java/ASMDemo/out/production/ASMDemo/modifyfield/TestModifyFiled.class";
        FileOutputStream outputStream = null;
        try {
            ClassReader classReader = new ClassReader(TestModifyFiled.class.getName());
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
            ClassVisitor modifyField = new ModifyFieldClassVisitor(ASM4, classWriter);
            classReader.accept(modifyField, ClassReader.SKIP_CODE);

            outputStream = new FileOutputStream(path);
            outputStream.write(classWriter.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
