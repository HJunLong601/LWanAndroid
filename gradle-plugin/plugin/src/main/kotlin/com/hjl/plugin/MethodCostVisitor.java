package com.hjl.plugin;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * author: long
 * description please add a description here
 * Date: 2021/9/30
 */
public class MethodCostVisitor extends AdviceAdapter {

    private int startTimeId = -1;

    private String methodName = null;

    /**
     * Constructs a new {@link AdviceAdapter}.
     *
     * @param api           the ASM API version implemented by this visitor. Must be one of {@link
     *                      Opcodes#ASM4}, {@link Opcodes#ASM5}, {@link Opcodes#ASM6} or {@link Opcodes#ASM7}.
     * @param methodVisitor the method visitor to which this adapter delegates calls.
     * @param access        the method's access flags (see {@link Opcodes}).
     * @param name          the method's name.
     * @param descriptor    the method's descriptor (see {@link Type Type}).
     */
    protected MethodCostVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
        methodName = name;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        startTimeId = newLocal(Type.LONG_TYPE);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        storeLocal(startTimeId);
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);

//        mv.visitLdcInsn("Test");
//        mv.visitLdcInsn("Test LOG");
//        mv.visitMethodInsn(INVOKESTATIC,"android/util/Log","i","(Ljava/lang/String;Ljava/lang/String;)I",false);
//        pop();

        int durationId = newLocal(Type.LONG_TYPE);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        mv.visitVarInsn(LLOAD, startTimeId);
        mv.visitInsn(LSUB);
        storeLocal(durationId);

        mv.visitLdcInsn("TimeStatistics");
        mv.visitTypeInsn(NEW,"java/lang/StringBuilder");
        dup();
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitLdcInsn(methodName + " cost time :");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        loadLocal(durationId);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
        mv.visitLdcInsn(" ms");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKESTATIC,"android/util/Log","i","(Ljava/lang/String;Ljava/lang/String;)I",false);
        pop();


        //
//        mv.visitLdcInsn("TimeStatistics");
//        mv.visitTypeInsn(NEW,"java/lang/StringBuilder");
//        dup();
//        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
//        mv.visitLdcInsn(methodName + " cost time :");
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
//        mv.visitVarInsn(LLOAD, startTimeId);
//        loadLocal(startTimeId);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//        mv.visitLdcInsn(" ms");
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
//        mv.visitMethodInsn(INVOKESTATIC,"android/util/Log","i","(Ljava/lang/String;Ljava/lang/String;)I",false);
//
//        int durationId = newLocal(Type.LONG_TYPE);
//        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
//        mv.visitVarInsn(LLOAD, startTimeId);
//        mv.visitInsn(LSUB);
//        storeLocal(durationId);
//        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
//        dup();
//        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
//        mv.visitLdcInsn("The cost time of " + methodName + "() is ");
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//        loadLocal(durationId);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
//        mv.visitLdcInsn(" ms");
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }
}
