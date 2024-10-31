plugins {
    kotlin("multiplatform") version "1.9.23"
    id("com.android.application") version "8.2.0"
}

group = "cub.taifin"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

kotlin {
    jvm() {

    }
    androidTarget()
}

android {
    namespace="cub.taifin"
    compileSdk=35
    sourceSets.all {
        manifest.srcFile("app/androidApp/src/main/AndroidManifest.xml")
    }
}