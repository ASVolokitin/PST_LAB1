plugins {
    kotlin("jvm") version "2.2.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}