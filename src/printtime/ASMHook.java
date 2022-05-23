package printtime;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ASMHook implements Opcodes {
    public static final String PATH = "/Users/moo/workspace/java/ASMDemo/out/production/ASMDemo/printtime/TestPrintTime.class";

    public static void changeLog() {
        byte[] code;
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(PATH);
            ClassReader classReader = new ClassReader(inputStream);
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
            ClassVisitor classVisitor = new ChangeVisitor("TestPrintTime.class", ASM4, classWriter);
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
            code = classWriter.toByteArray();

            outputStream = new FileOutputStream(PATH);
            outputStream.write(code);
            outputStream.close();
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

    public static void main(String[] args) {
//        changeLog();
        new TestPrintTime().loadData();
    }
}
