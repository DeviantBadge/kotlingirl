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
        classpath("org.jetbrains.kotlin:kotlin-noarg:${kotlinVersion}")
    }
}

plugins {
    java
}

apply(plugin = "java-library")
apply(plugin = "java")
apply(plugin = "idea")

apply(plugin = "kotlin")
apply(plugin = "kotlin-jpa")
apply(plugin = "kotlin-spring")
apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
}

dependencies {
    implementation(project(":kotlingirl-configuration"))

    implementation("com.alibaba", "fastjson", "1.2.54")
    implementation("postgresql", "postgresql", "9.3-1102.jdbc41")
    implementation("io.github.rybalkinsd", "kohttp", "0.3.1")
    implementation("org.slf4j", "slf4j-api", "1.7.25")

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation(springBoot("web"))
    implementation(springBoot("actuator"))
    implementation(springBoot("data-jpa"))
    implementation(springBoot("data-rest"))

    implementation(springCloud("netflix-ribbon"))
    implementation(springCloud("openfeign"))
    implementation(springCloud("netflix-eureka-client"))

    testImplementation("junit", "junit", "4.12")
    testImplementation(springBoot("test"))
}

ext["springCloudVersion"] = "Greenwich.M3"
ext["springBootVersion"] = "2.0.5.RELEASE"
configure<DependencyManagementExtension> {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${ext["springCloudVersion"]}")
        mavenBom("org.springframework.boot:spring-boot-dependencies:${ext["springBootVersion"]}")
    }
}

fun springBoot(module: String, version: String? = null) =
        "org.springframework.boot:spring-boot-starter-$module${version?.let { ":$version" } ?: ""}"

fun springCloud(module: String, version: String = "2.0.2.RELEASE") =
        "org.springframework.cloud:spring-cloud-starter-$module:$version"
