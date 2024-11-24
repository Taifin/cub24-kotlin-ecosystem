plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    id("com.android.library")
}

repositories {
    mavenCentral()
    google()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

android {
    namespace="cub.taifin.shared"
    compileSdk=35
    sourceSets.all {
        manifest.srcFile("../app/androidApp/src/main/AndroidManifest.xml")
    }
    defaultConfig {
        minSdk=21
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

                implementation(libs.compose.ui)
                implementation(libs.compose.ui.util)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.compiler)
                implementation(libs.compose.runtime)

                implementation(libs.compose.material.icons.core)
                implementation(libs.compose.material.icons.extended)

                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.cio)

                implementation(project(":server"))
            }
        }
        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.androidx.activity.compose)
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.multidex)
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