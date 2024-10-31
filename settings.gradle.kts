pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "cub24-kotlin-ecosystem"

include(":shared")
include(":app:androidApp")
include(":app:desktopApp")