import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask

val assertJVersion = "3.11.1"
val coverageThreshold = 0.90
val jacocoToolVersion = "0.8.2"
val jupiterVersion = "5.3.2"
val jvmTargetVersion = "1.8"
val publicationName = "maven"

plugins {
    jacoco
    `maven-publish`
    kotlin("jvm") version "1.3.11"
    id("com.github.nwillc.vplugin") version "2.3.0"
    id("org.jmailen.kotlinter") version "1.20.1"
    id("io.gitlab.arturbosch.detekt") version "1.0.0.RC9.2"
    id("com.github.ngyewch.git-version") version "0.2"
    id("com.jfrog.bintray") version "1.8.4"
}

group = "com.github.nwillc"
version = gitVersion.gitVersionInfo.gitVersionName.substring(1)

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

gitVersion {
    gitTagPrefix = "v"
}

jacoco {
    toolVersion = jacocoToolVersion
}

detekt {
    input = files("src/main/kotlin")
    filters = ".*/build/.*"
}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create<MavenPublication>(publicationName) {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}


bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_API_KEY")
    dryRun = false
    publish = true
    setPublications(publicationName)
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = publicationName
        name = project.name
        desc = "Kotlin Either and Try."
        websiteUrl = "https://github.com/nwillc/ket"
        issueTrackerUrl = "https://github.com/nwillc/ket/issues"
        vcsUrl = "https://github.com/nwillc/ket.git"
        version.vcsTag = gitVersion.gitVersionInfo.gitVersionName
        setLicenses("ISC")
        setLabels("kotlin", "Either", "Try")
        publicDownloadNumbers = true
    })
}

tasks {
    jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    minimum = coverageThreshold.toBigDecimal()
                }
            }
        }
    }
    named("check") {
        dependsOn(":jacocoTestCoverageVerification")
    }
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
