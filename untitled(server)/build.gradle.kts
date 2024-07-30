plugins {
    application
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
    kotlin("jvm") version "1.9.23"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation:2.3.10")
    implementation("io.ktor:ktor-server-core-jvm:2.3.10")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.10")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.10")
    implementation("io.ktor:ktor-server-default-headers-jvm:2.3.10")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.10")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.postgresql:postgresql")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.38.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}