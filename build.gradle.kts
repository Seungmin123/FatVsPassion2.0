plugins {
    kotlin("jvm") version "1.7.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.telegram:telegrambots:5.3.0")
    implementation("org.xerial:sqlite-jdbc:3.36.0.1")
}

val fatVsPassion = "com.whalee.FatVsPassionKt"

tasks.jar {
    manifest {
        attributes["Main-Class"] = fatVsPassion
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}