package me.tangni.cybertron

import com.android.SdkConstants
import com.android.build.api.transform.*
import com.android.ide.common.internal.WaitableExecutor
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOCase
import org.apache.commons.io.filefilter.SuffixFileFilter
import org.apache.commons.io.filefilter.TrueFileFilter
import org.gradle.api.Project
import java.io.*

abstract class Cybertron(private val project: Project, private val classEditor: CybertronClassEditor) : Transform() {

    private var waitableExecutor: WaitableExecutor = WaitableExecutor.useGlobalSharedThreadPool()
//    private var logger: Logger = Logger.fromProject(project, "${project.displayName}-Cybertron-$name")

//    override fun getName(): String {
//    }
//
//    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
//    }
//
//    override fun isIncremental(): Boolean {
//    }
//
//    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
//    }

    override fun transform(transformInvocation: TransformInvocation) {
        val isIncremental = transformInvocation.isIncremental
//        logger.i("transform begins, isIncremental: $isIncremental")

        val outputProvider = transformInvocation.outputProvider
        if (outputProvider == null) {
//            logger.e("outputProvider null!")
            return
        }

        val supportsIncremental = isIncremental()
        val isIncrementalBuild = transformInvocation.isIncremental

        for (input in transformInvocation.inputs) {
            for (jarInput in input.jarInputs) {
                transformJarInput(jarInput, outputProvider, supportsIncremental, isIncrementalBuild)
            }

            for (dirInput in input.directoryInputs) {
                transformDirInput(dirInput, outputProvider, supportsIncremental, isIncrementalBuild)
            }
        }

        waitableExecutor.waitForTasksWithQuickFail<Void>(true)
    }

    @Throws(IOException::class)
    private fun transformDirInput(dirInput: DirectoryInput, outputProvider: TransformOutputProvider,
                                  supportsIncremental: Boolean, isIncrementalBuild: Boolean) {

        val dest: File = outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes,
                dirInput.scopes, Format.DIRECTORY)

        val srcDirPath: String = dirInput.file.absolutePath
        val destDirPath: String = dest.absolutePath

        if (supportsIncremental && isIncrementalBuild) {

            val changedFiles: Map<File, Status> = dirInput.changedFiles

            for (changedFile in changedFiles) {
                val inputFile = changedFile.key
                val destFile = File(inputFile.absolutePath.replace(srcDirPath, destDirPath))
                when (changedFile.value) {
                    Status.REMOVED -> {
                        if (destFile.exists()) {
                            destFile.delete()
                        }
                    }
                    Status.ADDED, Status.CHANGED -> {
                        FileUtils.touch(destFile)
                        transformFile(inputFile, destFile, srcDirPath)
                    }
                    Status.NOTCHANGED -> {
                    }
                }
            }
        } else {
            val dirFile: File = dirInput.file
            if (dirFile.isDirectory) {

                val files: Collection<File> = FileUtils.listFiles(dirFile,
                        SuffixFileFilter(SdkConstants.DOT_CLASS, IOCase.INSENSITIVE), TrueFileFilter.INSTANCE)

                for (file in files) {
                    val outputFile = File(file.absolutePath.replace(srcDirPath, destDirPath))
                    transformFile(file, outputFile, srcDirPath)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun transformJarInput(jarInput: JarInput, outputProvider: TransformOutputProvider,
                                  supportsIncremental: Boolean, isIncrementalBuild: Boolean) {

        val status: Status = jarInput.status
        val dest: File = outputProvider.getContentLocation(
                jarInput.file.absolutePath,
                jarInput.contentTypes,
                jarInput.scopes,
                Format.JAR)

        if (supportsIncremental && isIncrementalBuild) {
            when (status) {
                Status.REMOVED -> {
                    if (dest.exists()) {
                        FileUtils.forceDelete(dest)
                    }
                }
                Status.ADDED, Status.CHANGED -> {
                    transformJar(jarInput.file, dest, status)
                }
                Status.NOTCHANGED -> {
                }
            }
        } else {
            transformJar(jarInput.file, dest, status)
        }

    }

    @Throws(IOException::class)
    private fun transformJar(src: File, dest: File, status: Status) {
//        waitableExecutor.execute {
//            //TODO
//        }
    }

    @Throws(IOException::class)
    private fun transformFile(inputFile: File, outputFile: File, srcBaseDir: String) {
        val fullQualifiedClassName = inputFile.absolutePath.replace(srcBaseDir, "").replace(File.separator, "")
        if (willEditClass(fullQualifiedClassName)) {

            val inputClassFileInputStream = FileInputStream(inputFile)
            val editedByteArray = getEditedClassByteArray(inputClassFileInputStream, fullQualifiedClassName)
            editedByteArray?.let {
                FileOutputStream(outputFile).apply {
                    write(it)
                    close()
                }
                inputClassFileInputStream.close()
            }?:justCopy(inputFile, outputFile)
        } else {
            justCopy(inputFile, outputFile)
        }
    }

    private fun justCopy(inputFile: File, outputFile: File) {
        if (inputFile.isFile) {
            FileUtils.copyFile(inputFile, outputFile)
        }
    }

    /**
     * return true if you want to edit this class
     *
     * @param fullQualifiedClassName
     * @return
     */
    protected open fun willEditClass(fullQualifiedClassName: String): Boolean {
        return fullQualifiedClassName.endsWith(".class")
    }

    /**
     * returning null means you don't want to edit this class
     *
     * @param inputStream
     * @param fullQualifiedClassName
     * @return the edited class's byte array, or null
     */
    @Throws(IOException::class)
    private fun getEditedClassByteArray(inputStream: InputStream, fullQualifiedClassName: String): ByteArray? {
        return classEditor.getEditedClassByteArray(inputStream, fullQualifiedClassName)
    }

}
