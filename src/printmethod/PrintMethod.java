package printmethod;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import printmethod.custom.PrintClassVisitor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PrintMethod implements Opcodes {
    public static final String PATH1 = "/Users/moo/workspace/java/ASMDemo/out/production/ASMDemo/printmethod/custom/PrintAnnotationVisitor.class";
    public static final String PATH2 = "/Users/moo/workspace/java/ASMDemo/out/production/ASMDemo/printmethod/custom/PrintClassVisitor.class";
    public static final String PATH3 = "/Users/moo/workspace/java/ASMDemo/out/production/ASMDemo/printmethod/custom/PrintFieldVisitor.class";
    public static final String PATH4 = "/Users/moo/workspace/java/ASMDemo/out/production/ASMDemo/printmethod/custom/PrintMethodVisitor.class";
    public static final String[] PATHS = new String[]{PATH1, PATH2, PATH3, PATH4};

    public static void addPrint() {
        for (int i = 0; i < PATHS.length; i++) {
            String path = PATHS[i];
            byte[] code;
            FileInputStream inputStream = null;
            FileOutputStream outputStream = null;
            try {
                inputStream = new FileInputStream(path);
                ClassReader classReader = new ClassReader(inputStream);
                ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
                ClassVisitor classVisitor = new PrintVisitor(path.substring(path.lastIndexOf("/") + 6, path.lastIndexOf(".class")), ASM4, classWriter);
                classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
                code = classWriter.toByteArray();
                outputStream = new FileOutputStream(path);
                outputStream.write(code);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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

    public static void printClass(){
        try {
            ClassReader cr  = new ClassReader(TestPrintClass.class.getName());
            ClassWriter classWriter = new ClassWriter(cr,ASM4);
            ClassVisitor cv = new PrintClassVisitor(ASM4,classWriter);
            cr.accept(cv,0);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        addPrint();
        printClass();
    }
}
