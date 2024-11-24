package cub.taifin.projectInfo

import org.gradle.api.Project
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.writeLines

// this and PrepareSourcesInfo can be generified even further obviously, but
abstract class PrepareBuildInfo : FileOutputTask(Paths.get("build-info.txt")) {

    @Internal
    val buildDirsByProject = project.allprojects.fold(mutableMapOf<Project, Set<File>>()) { acc, project ->
        acc[project] = getBuildDirsFromProject(project)
        acc
    }

    @TaskAction
    fun exec() {
        val classFilesByProject = buildDirsByProject.mapValues { (_, dirs) ->
            dirs.flatMap {
                it.walk().filter { file ->
                    file.isFile && file.extension == "class"
                }
            }
        }

        val totalAmountString =
            "Amount of .class files in the project ${classFilesByProject.entries.fold(0) { acc, entry -> acc + entry.value.size }}"
        val byProjectAmountBuilder = StringBuilder()
        classFilesByProject.forEach {
            byProjectAmountBuilder.append("\t${it.key}: ${it.value.size}\n")
        }

        outputFile.writeLines(listOf(totalAmountString, byProjectAmountBuilder.toString()))
    }

    private fun getBuildDirsFromProject(project: Project): Set<File> {
        val buildDirs = mutableSetOf<File>();
        project.allprojects.forEach { subProject ->
            subProject.tasks.withType(org.gradle.api.tasks.compile.JavaCompile::class.java).forEach { javaTask ->
                val outputDir = javaTask.destinationDirectory.get().asFile
                if (outputDir.exists()) {
                    buildDirs.add(outputDir)
                }
            }

            subProject.tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java)
                .forEach { kotlinTask ->
                    val outputDir = kotlinTask.destinationDirectory.get().asFile
                    if (outputDir.exists()) {
                        buildDirs.add(outputDir)
                    }
                }
        }

        return buildDirs
    }

}