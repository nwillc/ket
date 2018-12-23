import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val assertJVersion = "3.11.1"
val jupiterVersion = "5.3.2"
val jvmTargetVersion = "1.8"

plugins {
    kotlin("jvm") version "1.3.11"
    id("com.github.nwillc.vplugin") version "2.3.0"
}

group = "com.github.nwillc"
version = "1.0-SNAPSHOT"

logger.lifecycle("${project.name} $version")

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    withType<Test> {
        useJUnitPlatform()
        testLogging.showStandardStreams = true
    }
    withType<JacocoReport> {
        dependsOn("test")
        reports {
            xml.apply {
                isEnabled = true
            }
            html.apply {
                isEnabled = true
            }
        }
    }
}