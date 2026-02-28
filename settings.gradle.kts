rootProject.name = "lab_1"

include("task_1", "task_2", "task_3")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("libs")
    }
}