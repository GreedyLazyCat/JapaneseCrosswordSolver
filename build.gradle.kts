plugins {
    kotlin("jvm") version "1.9.23"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "org.example.MainKt"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
