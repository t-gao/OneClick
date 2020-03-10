package me.tangni.oneclick.gradleplugin.transforms.asm

import me.tangni.oneclick.gradleplugin.utils.Logger
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class OneClickClassVisitor(private val className:String, cv: ClassVisitor) : ClassVisitor(Opcodes.ASM5, cv) {
    override fun visitMethod(access: Int, name: String?, desc: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        val mv = super.visitMethod(access, name, desc, signature, exceptions)
        return if ("onClick" == name && "(Landroid/view/View;)V" == desc) {
            Logger.lifecycle("OneClickClassVisitor visiting onClick method, signature: $signature, class: $className")
            OneClickMethodVisitor(className, mv, access, name, desc)
        } else {
            mv
        }
    }
}