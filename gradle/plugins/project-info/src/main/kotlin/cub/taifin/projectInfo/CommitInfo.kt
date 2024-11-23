package cub.taifin.projectInfo

import org.eclipse.jgit.api.Git
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class CommitInfo : DefaultTask() {
    @InputFiles
    val gitRootDir = project.rootDir

    @InputFiles
    val gitHeadsFile = File(gitRootDir, ".git/HEAD")

    @InputFiles
    val gitRefsFile = File(gitRootDir, ".git/refs")

    @TaskAction
    fun exec() {
        try {
            val repoDir = project.rootDir

            val git = Git.open(repoDir)
            val commits = git.log().setMaxCount(1).call()
            val commit = commits.iterator().next()
            val commitInfo = """
                Latest commit hash: ${commit.name}
                Author: ${commit.authorIdent.name}
                Message: ${commit.fullMessage}
            """.trimIndent()

            logger.lifecycle(commitInfo)
        } catch (e: Exception) {
            logger.error("Unable to fetch information about commits in the repository:\t\n$e")
        }
    }
}