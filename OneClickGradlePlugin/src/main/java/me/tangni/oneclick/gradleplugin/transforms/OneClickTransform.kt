package me.tangni.oneclick.gradleplugin.transforms

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import me.tangni.cybertron.Cybertron
import me.tangni.oneclick.gradleplugin.OneClickExtension
import me.tangni.oneclick.gradleplugin.transforms.asm.OneClickAsmClassEditor
import me.tangni.oneclick.gradleplugin.utils.Logger
import org.gradle.api.Project

class OneClickTransform(private val project: Project, private val isLibrary: Boolean = false)
    : Cybertron(project) {

    init {
        Logger.i("OneClickTransform constructor(), isLibrary: $isLibrary, project: ${project.displayName}")
        registerCybertronClassEditor(OneClickAsmClassEditor(this))
    }

    private var _clickInterval: Long = 0
    internal val clickInterval
        get() = _clickInterval

    override fun getName(): String = "OneClick"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS

    override fun isIncremental(): Boolean = false

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.PROJECT_ONLY

    override fun willEditClass(fullQualifiedClassName: String): Boolean {
        return super.willEditClass(fullQualifiedClassName)
    }

    override fun transform(transformInvocation: TransformInvocation) {
        Logger.i("transform begins")
        val oneClickExtension = project.extensions.findByType(OneClickExtension::class.java)
        if (oneClickExtension == null) {
            Logger.i("NO CONFIGS")
        }
        _clickInterval = oneClickExtension?.clickInterval?:0L

        Logger.i("oneClickExtension: clickInterval = $clickInterval")

        super.transform(transformInvocation)
    }

}
