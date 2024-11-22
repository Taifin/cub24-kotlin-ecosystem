plugins {
    kotlin("multiplatform") version "2.0.20"
    kotlin("plugin.compose") version "2.0.20"
    id("com.android.application") version "8.5.2"
}

group = "cub.taifin"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {}
    androidTarget()
}

android {
    namespace="cub.taifin"
    compileSdk=35
    sourceSets.all {
        manifest.srcFile("app/androidApp/src/main/AndroidManifest.xml")
    }
}