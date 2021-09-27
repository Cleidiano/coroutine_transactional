import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {

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


allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {

    version = "1.0"
    group = "br.com.demo.coroutine_transactional"

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")

    dependencies {

        implementation("org.springframework:spring-context:5.3.9")
//        implementation("org.springframework:spring-aop:5.3.9")
//        implementation("org.aspectj:aspectjweaver:1.9.7")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

        implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.30")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.30")

        implementation("ch.qos.logback:logback-classic:1.2.3")
        implementation("net.logstash.logback:logstash-logback-encoder:6.6")

        testImplementation("com.h2database:h2")
        testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
        testImplementation("io.kotest:kotest-assertions-core:4.6.2")
        testImplementation("io.kotest:kotest-runner-junit5:4.6.2")
        testImplementation("io.kotest:kotest-property-jvm:4.6.2")
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
}
