import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * author: long
 * description please add a description here
 * Date: 2021/10/8
 */
public class TestClassVisitor extends ClassVisitor {


    public TestClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM4, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);

        return new MethodCostVisitor(Opcodes.ASM4,methodVisitor,access,name,descriptor);
    }
}
