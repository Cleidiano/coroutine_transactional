plugins {
    id("io.ebean") version "12.11.3"
    kotlin("kapt")
}

dependencies {
    implementation(project(":usecases"))

    implementation("io.ebean:ebean:12.11.3")
    kapt("io.ebean:kotlin-querybean-generator:12.11.3")

    testImplementation("org.springframework:spring-test:5.3.9")
    testImplementation("io.ebean:ebean-test:12.11.3")
}
