package me.tangni.oneclick.gradleplugin

import com.android.build.gradle.AppExtension
import me.tangni.oneclick.gradleplugin.transforms.OneClickTransform
import me.tangni.oneclick.gradleplugin.utils.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project

class OneClickPlugin : Plugin<Project> {

//    private lateinit var logger: Logger

    override fun apply(project: Project) {
//        logger = Logger.fromProject(project, "OneClick::Gradle Plugin >>> ")
        Logger.init(project)
        Logger.i("OneClickPlugin apply(), project: ${project.displayName}")

        val extension = project.extensions.findByType(AppExtension::class.java)

        extension?.apply {
            registerTransform(OneClickTransform(project))
        }
    }

//    companion object {
//        const val LOG_TAG = "OneClick::Gradle Plugin >>> "
//    }
}
