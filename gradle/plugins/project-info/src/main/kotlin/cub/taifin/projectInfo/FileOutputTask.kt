package cub.taifin.projectInfo

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.notExists

abstract class FileOutputTask(
    outputFileName: Path
) : DefaultTask() {

    @get:OutputFile
    val outputFile: Path =  project.layout.buildDirectory.asFile.get().toPath().resolve(outputFileName)

    init {
        val projectBuildDir = project.layout.buildDirectory.asFile.get()
        if (!projectBuildDir.exists()) {
            projectBuildDir.mkdirs()
        }

        if (outputFile.notExists()) {
            outputFile.createFile()
        }
    }
}