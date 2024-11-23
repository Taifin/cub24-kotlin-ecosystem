import cub.taifin.projectInfo.CommitInfo
import cub.taifin.projectInfo.ProjectInfo

val projectInfo = extensions.create<ProjectInfo>("project-info")

val piGroup = "project-info"

val commitInfoTask = tasks.register<CommitInfo>("commit-info") {
    group = piGroup
    description = "output latest commit information"
}