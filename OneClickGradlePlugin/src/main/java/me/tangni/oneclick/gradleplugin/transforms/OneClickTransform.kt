package me.tangni.oneclick.gradleplugin.transforms

import com.android.build.api.transform.QualifiedContent
import com.android.build.gradle.internal.pipeline.TransformManager
import me.tangni.cybertron.Cybertron
import org.gradle.api.Project

class OneClickTransform(private val project: Project) : Cybertron(project, OneClickClassEditor()) {

    override fun getName(): String = "OneClick"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS

    override fun isIncremental(): Boolean = false

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.PROJECT_ONLY

    override fun willEditClass(fullQualifiedClassName: String): Boolean {
        return super.willEditClass(fullQualifiedClassName)
    }
}