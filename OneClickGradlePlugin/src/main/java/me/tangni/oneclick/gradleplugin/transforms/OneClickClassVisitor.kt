package me.tangni.oneclick.gradleplugin.transforms

import me.tangni.oneclick.gradleplugin.utils.Logger
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class OneClickClassVisitor(cv: ClassVisitor) : ClassVisitor(Opcodes.ASM5, cv) {
    override fun visitMethod(access: Int, name: String?, desc: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        val mv = super.visitMethod(access, name, desc, signature, exceptions)
        return if ("onClick" == name && "(Landroid/view/View;)V" == desc) {
            Logger.i("OneClickClassVisitor visiting onClick method")
            OneClickMethodVisitor(mv, access, name, desc)
        } else {
            mv
        }
    }
}