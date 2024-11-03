plugins {
    application
    kotlin("jvm")
    id("io.ktor.plugin") version "3.0.0"
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-core:${libs.versions.ktor}")
    implementation("io.ktor:ktor-server-status-pages:${libs.versions.ktor}")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty:${libs.versions.ktor}")
    implementation("io.ktor:ktor-server-auth:${libs.versions.ktor}")
    implementation(libs.logback)
    testImplementation("io.ktor:ktor-server-test-host-jvm:${libs.versions.ktor}")
    testImplementation(libs.kotlin.test)
}