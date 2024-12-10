import org.gradle.kotlin.dsl.android

plugins {
    kotlin("android")
    id("com.android.application")
    kotlin("plugin.compose") version "2.0.20" // this version matches your Kotlin version\
}

repositories {
    google()
    mavenCentral()
}

android {
    namespace = "cub.taifin"
    compileSdk = 35

    sourceSets.all {
        manifest.srcFile("src/main/AndroidManifest.xml")
    }

    defaultConfig {
        applicationId = "cub.taifin"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "META-INF/*"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.logback.android)
}