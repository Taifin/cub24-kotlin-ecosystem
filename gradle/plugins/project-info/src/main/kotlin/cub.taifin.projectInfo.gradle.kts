import cub.taifin.projectInfo.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val projectInfo = extensions.create<ProjectInfo>("project-info")

val piGroup = "project-info"

val commitInfoTask = tasks.register<CommitInfo>("commit-info") {
    group = piGroup
    description = "output latest commit information"
}

val prepareSourcesInfoTask = tasks.register<PrepareSourcesInfo>("prepare-sources-info") {
    group = piGroup
    description = "collect information about sources in project and all subprojects"

   sourceDirsByProject.entries.forEach { entry ->
       val dirs = entry.value
       dirs.forEach { dir ->
           if (dir.exists() && dir.isDirectory)
               inputs.dir(dir)
       }
    }
}

val sourcesInfoTask = tasks.register<FileTracker>("sources-info") {
    group = piGroup
    description = "output information about source files in project and all subprojects"

    trackedFile = prepareSourcesInfoTask.get().outputFile

    dependsOn(prepareSourcesInfoTask)
}

val prepareBuildInfoTask = tasks.register<PrepareBuildInfo>("prepare-build-info") {
    group = piGroup
    description = "collect information about build .class files in the project"

    project.allprojects.forEach { subProject ->
        subProject.tasks.withType(JavaCompile::class.java).forEach {
            dependsOn(it)
        }
        subProject.tasks.withType(KotlinCompile::class.java).forEach {
            dependsOn(it)
        }
    }

    // likely unnecessary (this task will be triggered anyway if anything is recompiled by tasks it depends on),
    // but why not :)
    buildDirsByProject.entries.forEach { entry ->
        val dirs = entry.value
        dirs.forEach { dir ->
            if (dir.exists() && dir.isDirectory)
                inputs.dir(dir)
        }
    }
}

val buildInfoTask = tasks.register<FileTracker>("build-info") {
    group = piGroup
    description = "output information about build .class files in the project"

    trackedFile = prepareBuildInfoTask.get().outputFile

    dependsOn(prepareBuildInfoTask)
}