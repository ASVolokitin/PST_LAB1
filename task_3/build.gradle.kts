plugins {
    kotlin("jvm") version "2.2.20"
    id("com.xcporter.metaview") version "0.0.5"
}

group = "com.sashka"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
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

tasks {
    generateUml {
//        target.set(projectDir.parentFile.resolve("src/main"))
        classTree {
            target; "src/main/"
        }
    }
}