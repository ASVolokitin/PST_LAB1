plugins {
    kotlin("jvm") version "2.2.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.knowm.xchart:xchart:3.8.8")
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-property:6.1.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
}

tasks.named("clean") {
    doFirst {
        delete("plot_output", "csv_output")
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}