import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources

plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    id("com.android.library")
    id("org.jetbrains.compose")
}

repositories {
    mavenCentral()
    google()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

android {
    namespace="cub.taifin"
    compileSdk=35
    sourceSets.all {
        manifest.srcFile("../app/androidApp/src/main/AndroidManifest.xml")
    }

}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)

                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.components.resources)
                implementation(libs.compose.material.icons.core)
                implementation(libs.compose.material.icons.extended)

                implementation(project.dependencies.platform(libs.androidx.compose.bom))


                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.cio)

                implementation(project(":server"))
            }
        }
        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.logback)
            }
        }
    }

    androidTarget()
    jvm()
}