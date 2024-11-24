package cub.taifin.projectInfo

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.writeLines

@CacheableTask
abstract class PrepareSourcesInfo : FileOutputTask(Paths.get("sources-info.txt")) {

    @Internal
    val sourceDirsByProject = project.allprojects.fold(mutableMapOf<Project, Set<File>>()) { acc, project ->
        acc[project] = getSourcesFromProject(project)
        acc
    }

    @TaskAction
    fun exec() {
        val sourceFilesByProject = sourceDirsByProject.mapValues { (_, dirs) ->
            dirs.flatMap {
                it.walk().filter { file ->
                    file.isFile && file.extension in listOf("java", "kt")
                }
            }
        }
        val totalAmountString =
            "Amount of source files in the project: ${sourceFilesByProject.entries.fold(0) { acc, entry -> acc + entry.value.size }}"
        val byProjectAmountBuilder = StringBuilder()
        sourceFilesByProject.forEach {
            byProjectAmountBuilder.append("\t${it.key}: ${it.value.size}\n")
        }

        outputFile.writeLines(listOf(totalAmountString, byProjectAmountBuilder.toString()))
    }

    private fun getSourcesFromProject(project: Project): Set<File> {
        val sourceDirs = mutableSetOf<File>()

        project.extensions.findByType(JavaPluginExtension::class.java)?.let { javaPluginExt ->
            javaPluginExt.sourceSets.forEach { sourceSet ->
                sourceDirs.addAll(sourceSet.allSource.srcDirs)
            }
        }

        sourceDirs.addAll(
            project.extensions.findByType(KotlinProjectExtension::class.java)
                ?.sourceSets
                ?.flatMap { it.kotlin.sourceDirectories }
                ?.toSet()
                ?: emptySet()
        )

        return sourceDirs
    }
}