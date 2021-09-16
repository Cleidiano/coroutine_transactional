import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.ebean") version "12.11.3"
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.30"
    kotlin("kapt") version "1.5.30"
    kotlin("plugin.spring") version "1.5.30"
    kotlin("plugin.jpa") version "1.5.30"
}

group = "br.com.demo"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ebean:ebean:12.11.3")
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework:spring-context:5.3.9")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    kapt("io.ebean:kotlin-querybean-generator:12.11.3")

    testImplementation("com.h2database:h2")
    testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
    testImplementation("io.kotest:kotest-assertions-core:4.6.2")
    testImplementation("io.kotest:kotest-runner-junit5:4.6.2")
    testImplementation("io.kotest:kotest-property-jvm:4.6.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.ebean:ebean-test:12.11.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
