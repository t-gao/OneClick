package me.tangni.oneclick.gradleplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import me.tangni.oneclick.gradleplugin.transforms.OneClickTransform
import me.tangni.oneclick.gradleplugin.utils.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project

class OneClickPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        Logger.init(project)
        Logger.i("OneClickPlugin apply(), project: ${project.displayName}")

        project.extensions.findByType(AppExtension::class.java)?.apply {
            registerTransform(OneClickTransform(project, false))
        }?:project.extensions.findByType(LibraryExtension::class.java)?.apply {
            registerTransform(OneClickTransform(project, true))
        }
    }
}
