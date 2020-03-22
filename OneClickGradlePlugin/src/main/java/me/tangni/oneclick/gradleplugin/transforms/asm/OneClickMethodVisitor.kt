package me.tangni.oneclick.gradleplugin.transforms.asm

import me.tangni.oneclick.gradleplugin.utils.Logger
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class OneClickMethodVisitor(mv: MethodVisitor?,
                            access: Int,
                            name: String?,
                            desc: String?,
                            private val className:String,
                            private val clickInterval: Long) : AdviceAdapter(Opcodes.ASM5, mv, access, name, desc) {

    /**
     * insert below code at the beginning of the onClick(View v) method:
     *     if (OneClick.isFastClick(v, "listenerClassName", clickInterval)) {
     *         return;
     *     }
     */
    override fun onMethodEnter() {
        Logger.i("OneClickMethodVisitor onMethodEnter, className: $className")
        mv.visitVarInsn(Opcodes.ALOAD, 1)
        mv.visitLdcInsn(className)
        if (clickInterval > 0) {
            mv.visitLdcInsn(clickInterval)
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "me/tangni/oneclick/OneClick", "isFastClick", "(Landroid/view/View;Ljava/lang/String;J)Z", false)
        } else {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "me/tangni/oneclick/OneClick", "isFastClick", "(Landroid/view/View;Ljava/lang/String;)Z", false)
        }
        val nl1 = Label()
        mv.visitJumpInsn(Opcodes.IFEQ, nl1)
        val nl2 = Label()
        mv.visitLabel(nl2)
        mv.visitInsn(Opcodes.RETURN)
        mv.visitLabel(nl1)
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null)
    }

}