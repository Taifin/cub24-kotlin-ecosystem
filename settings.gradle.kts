pluginManagement {
    includeBuild("gradle/plugins")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("plugin.compose") version "2.0.20"
        id("org.jetbrains.compose") version "1.7.0"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "cub24-kotlin-ecosystem"

include(":shared")
include(":app:androidApp")
include(":app:desktopApp")
include(":server")
include("dsl")
