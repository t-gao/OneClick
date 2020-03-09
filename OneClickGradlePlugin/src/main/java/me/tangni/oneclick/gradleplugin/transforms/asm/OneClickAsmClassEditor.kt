package me.tangni.oneclick.gradleplugin.transforms.asm

import me.tangni.cybertron.Cybertron
import me.tangni.cybertron.CybertronClassEditor
import me.tangni.cybertron.utils.ClassHelper
import me.tangni.oneclick.gradleplugin.utils.Logger
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.InputStream

class OneClickAsmClassEditor : CybertronClassEditor {

    override fun getEditedClassByteArray(cybertron: Cybertron,
                                         inputStream: InputStream,
                                         fullQualifiedClassName: String): ByteArray? {
        val classReader = ClassReader(inputStream)

        val cn = classReader.className
        Logger.i("getEditedClassByteArray of class $cn")

        val clickListenerClassName = "android/view/View\$OnClickListener"
        var interesting = classReader.interfaces.contains(clickListenerClassName)
        if (interesting) {
            Logger.i("$cn implements OnClickListener by itself!")
        }
        if (!interesting) {
            val allInterfaces = ClassHelper.getAllInterfaces(cybertron.classLoader,
                    cn.replace(File.separatorChar, '.'))
            if (allInterfaces?.contains(clickListenerClassName.replace(File.separatorChar, '.')) == true) {
                Logger.i("$cn is found interesting from it's superclasses, direct super: ${classReader.superName}")
                interesting = true
            }
        }

        return if (interesting) {
            Logger.i("$cn is interesting!")
            val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
            val classVisitor = OneClickClassVisitor(classWriter)
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
            classWriter.toByteArray()
        } else {
            null
        }

    }
}