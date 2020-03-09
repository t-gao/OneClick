package me.tangni.oneclick.gradleplugin.transforms

import me.tangni.oneclick.gradleplugin.utils.Logger
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class OneClickMethodVisitor(mv: MethodVisitor?,
                            access: Int,
                            name: String?,
                            desc: String?) : AdviceAdapter(Opcodes.ASM5, mv, access, name, desc) {

    /**
     * insert below code at the beginning of the onClick(View v) method:
     *     if (OneClick.isFastClick(v)) {
     *         return;
     *     }
     */
    override fun onMethodEnter() {
        Logger.i("OneClickMethodVisitor onMethodEnter")
        mv.visitVarInsn(Opcodes.ALOAD, 1)
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "me/tangni/oneclick/OneClick", "isFastClick", "(Landroid/view/View;)Z", false)
        val nl1 = Label()
        mv.visitJumpInsn(Opcodes.IFEQ, nl1)
        val nl2 = Label()
        mv.visitLabel(nl2)
//        mv.visitLineNumber(27, nl2)
        mv.visitInsn(Opcodes.RETURN)
        mv.visitLabel(nl1)
//        mv.visitLineNumber(29, nl1)
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null)
    }

}