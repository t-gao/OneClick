package me.tangni.oneclick.gradleplugin.transforms

import me.tangni.cybertron.CybertronClassEditor
import me.tangni.oneclick.gradleplugin.utils.Logger
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.InputStream

class OneClickClassEditor() : CybertronClassEditor {
    override fun getEditedClassByteArray(inputStream: InputStream, fullQualifiedClassName: String): ByteArray? {
        val classReader = ClassReader(inputStream)

        Logger.lifecycle("OneClickClassEditor getEditedClassByteArray, class $fullQualifiedClassName's interfaces:")
        for (itf in classReader.interfaces) {
            Logger.lifecycle(itf)
        }

        if (classReader.interfaces.contains("android/view/View\$OnClickListener")) {
            val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
            val classVisitor = OneClickClassVisitor(classWriter)
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
            return classWriter.toByteArray()
        }

        return null
    }
}