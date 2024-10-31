plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

repositories {
    mavenCentral()
    google()
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
            }
        }
        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
    }

    androidTarget()
    jvm()
}