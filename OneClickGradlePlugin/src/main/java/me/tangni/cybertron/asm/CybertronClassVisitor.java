package me.tangni.cybertron.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class CybertronClassVisitor extends ClassVisitor {
    public CybertronClassVisitor(int api) {
        super(api);
    }

    public CybertronClassVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        return mv;
    }
}
