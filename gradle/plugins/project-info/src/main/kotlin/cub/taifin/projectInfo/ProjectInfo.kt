package cub.taifin.projectInfo

import org.gradle.api.provider.Property
import java.io.File

interface ProjectInfo {
    val projectRoot: Property<File>
}