import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.internal.impldep.aQute.bnd.osgi.Analyzer
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

group = "com.kotlingirl"
version = "1.0-SNAPSHOT"

buildscript {
    val kotlinVersion = "1.3.0"
    val springBootVersion = "2.1.0.RELEASE"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-allopen:${kotlinVersion}")
    }
}

plugins {
    java
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
}

apply(plugin = "java")
apply(plugin = "idea")
apply(plugin = "kotlin")
apply(plugin = "kotlin-spring")
apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
}

dependencies {
//    implementation(project(":kotlingirl-configuration"))

    implementation("com.alibaba", "fastjson", "1.2.54")
    implementation("org.slf4j", "slf4j-api", "1.7.25")

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation(springBoot("actuator"))
    implementation(springBoot("webflux"))
    implementation(springBoot("data-redis-reactive"))

    implementation("org.springframework.cloud:spring-cloud-starter")
    implementation(springCloud("gateway"))
    implementation(springCloud("netflix-eureka-client"))

    testImplementation("junit", "junit", "4.12")
    testImplementation(springBoot("test"))
}

ext["springCloudVersion"] = "Greenwich.M3"
ext["springBootVersion"] = "2.0.5.RELEASE"
configure<DependencyManagementExtension> {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${ext["springCloudVersion"]}")
//        mavenBom("org.springframework.cloud:spring-cloud-gateway:2.0.1.RELEASE")
        mavenBom("org.springframework.boot:spring-boot-dependencies:${ext["springBootVersion"]}")
    }
}

fun springBoot(module: String, version: String? = null) =
        "org.springframework.boot:spring-boot-starter-$module${version?.let { ":$version" } ?: ""}"

fun springCloud(module: String, version: String? = null) =
        "org.springframework.cloud:spring-cloud-starter-$module${version?.let { ":$version" } ?: ""}"
