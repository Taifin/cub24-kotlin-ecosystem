plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.9.22"
}

dependencies {
    repositories {
        mavenCentral()
    }

    implementation("org.eclipse.jgit:org.eclipse.jgit:7.0.0.202409031743-r")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.20")
}