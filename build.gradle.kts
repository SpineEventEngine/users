/*
 * Copyright 2020, TeamDev. All rights reserved.
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

@file:Suppress("RemoveRedundantQualifierName") // To prevent IDEA replacing FQN imports.

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.spine.gradle.internal.DependencyResolution
import io.spine.gradle.internal.Deps
import io.spine.gradle.internal.PublishingRepos

buildscript {
    apply(from = "version.gradle.kts")
    apply(from = "$rootDir/config/gradle/dependencies.gradle")

    val dependencyResolution = io.spine.gradle.internal.DependencyResolution

    val kotlinVersion: String by extra
    val spineBaseVersion: String by extra
    val spineTimeVersion: String by extra

    dependencyResolution.defaultRepositories(repositories)
    dependencyResolution.forceConfiguration(configurations)

    configurations.all {
        resolutionStrategy {
            force(
                "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion",
                "org.jetbrains.kotlin:kotlin-stdlib-common:$kotlinVersion",
                "io.spine:spine-base:$spineBaseVersion",
                "io.spine:spine-time:$spineTimeVersion"
            )
        }
    }
}

plugins {
    java
    idea
    kotlin("jvm") version "1.4.21"
    id("com.google.protobuf").version(io.spine.gradle.internal.Deps.versions.protobufPlugin)
    id("net.ltgt.errorprone").version(io.spine.gradle.internal.Deps.versions.errorPronePlugin)
    id("io.spine.tools.gradle.bootstrap") version "1.6.15" apply false
}

apply(from = "version.gradle.kts")
val kotlinVersion: String by extra
val spineCoreVersion: String by extra
val spineBaseVersion: String by extra
val spineTimeVersion: String by extra

extra["projectsToPublish"] = listOf(
        "users-client",
        "users-server"
)
extra["credentialsPropertyFile"] = PublishingRepos.cloudRepo.credentials

allprojects {
    apply {
        plugin("jacoco")
        plugin("idea")
        plugin("project-report")
        apply(from = "$rootDir/version.gradle.kts")
    }

    group = "io.spine.users"
    version = extra["versionToPublish"]!!
}

subprojects {
    apply {
        plugin("java-library")
        plugin("kotlin")
        plugin("net.ltgt.errorprone")
        plugin("pmd")
        plugin("io.spine.tools.gradle.bootstrap")

        from(Deps.scripts.javacArgs(project))
        from(Deps.scripts.pmd(project))
        from(Deps.scripts.projectLicenseReport(project))
        from(Deps.scripts.testOutput(project))
        from(Deps.scripts.javadocOptions(project))

        from(Deps.scripts.testArtifacts(project))
    }

    val isTravis = System.getenv("TRAVIS") == "true"
    if (isTravis) {
        tasks.javadoc {
            val opt = options
            if (opt is CoreJavadocOptions) {
                opt.addStringOption("Xmaxwarns", "1")
            }
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    /* Uncomment, when Kotlin is used for the production code.

    val compileKotlin: KotlinCompile by tasks
    compileKotlin.kotlinOptions {
        jvmTarget = "1.8"
    }
    */

    val compileTestKotlin: KotlinCompile by tasks
    compileTestKotlin.kotlinOptions {
        jvmTarget = "1.8"
    }

    DependencyResolution.defaultRepositories(repositories)

    dependencies {
        errorprone(Deps.build.errorProneCore)
        errorproneJavac(Deps.build.errorProneJavac)

        /*  Uncomment, when Kotlin is used for the production code.
        implementation(kotlin("stdlib-jdk8"))
        */

        implementation(Deps.build.guava)
        implementation(Deps.build.jsr305Annotations)
        implementation(Deps.build.checkerAnnotations)
        Deps.build.errorProneAnnotations.forEach { implementation(it) }

        testImplementation(Deps.test.guavaTestlib)
        Deps.test.junit5Api.forEach { testImplementation(it) }

        testImplementation(kotlin("stdlib-jdk8"))
        testImplementation(Deps.test.junit5Runner)
        testImplementation("io.spine.tools:spine-mute-logging:$spineBaseVersion")
    }

    DependencyResolution.forceConfiguration(configurations)
    configurations {
        all {
            resolutionStrategy {
                force(
                    "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion",
                    "org.jetbrains.kotlin:kotlin-stdlib-common:$kotlinVersion",

                    "io.spine:spine-base:$spineBaseVersion",
                    "io.spine:spine-testlib:$spineBaseVersion",
                    "io.spine:spine-core:$spineCoreVersion",
                    "io.spine:spine-time:$spineTimeVersion"
                )
            }
        }
    }
    DependencyResolution.excludeProtobufLite(configurations)

    tasks.test {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
        }
    }

    apply {
        from(Deps.scripts.slowTests(project))
        from(Deps.scripts.testOutput(project))
        from(Deps.scripts.javadocOptions(project))
    }

    tasks.register("sourceJar", Jar::class) {
        from(sourceSets.main.get().allJava)
        archiveClassifier.set("sources")
    }

    tasks.register("testOutputJar", Jar::class) {
        from(sourceSets.test.get().output)
        archiveClassifier.set("test")
    }

    tasks.register("javadocJar", Jar::class) {
        from("$projectDir/build/docs/javadoc")
        archiveClassifier.set("javadoc")

        dependsOn(tasks.javadoc)
    }
}

apply {
    from(Deps.scripts.publish(project))

    // Aggregated coverage report across all subprojects.
    from(Deps.scripts.jacoco(project))

    // Generate a repository-wide report of 3rd-party dependencies and their licenses.
    from(Deps.scripts.repoLicenseReport(project))

    // Generate a `pom.xml` file containing first-level dependency of all projects in the repository.
    from(Deps.scripts.generatePom(project))
}
