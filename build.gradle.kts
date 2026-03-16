plugins {
    kotlin("jvm") version "2.2.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("io.kotest:kotest-runner-junit5:6.1.7")
    implementation("org.knowm.xchart:xchart:3.8.8")
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-property:6.1.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation("org.mockito:mockito-core:5.23.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.23.0")
}

tasks.named("clean") {
    doFirst {
        delete("plot_output", "csv_output")
    }
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
        showExceptions = true
        showCauses = true
        showStackTraces = true
        
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}
kotlin {
    jvmToolchain(17)
}