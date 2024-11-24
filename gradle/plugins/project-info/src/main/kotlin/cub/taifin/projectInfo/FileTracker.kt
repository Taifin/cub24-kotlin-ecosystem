package cub.taifin.projectInfo

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.nio.file.Path
import kotlin.io.path.readText

abstract class FileTracker : DefaultTask() {

    init {
        this.outputs.upToDateWhen { false }
    }

    @get:InputFile
    abstract val trackedFile: Property<Path>

    @TaskAction
    fun exec() {
        logger.lifecycle(trackedFile.get().readText())
    }
}