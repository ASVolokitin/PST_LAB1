plugins {
    kotlin("jvm") version "2.2.20"
    id("application")
    id("com.xcporter.metaview") version "0.0.5"
}

group = "com.sashka"
version = "1.0"

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.sashka.MainKt")
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-property:5.8.0")
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

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.sashka.MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}