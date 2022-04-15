/*
 * Copyright © 2014-2021 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.api.JavaVersion
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
  id("com.vanniktech.maven.publish.base")
  id("signing")
}

configure<MavenPublishBaseExtension> {
  group = requireNotNull(project.findProperty("GROUP"))
  version = requireNotNull(project.findProperty("VERSION_NAME"))
  mavenPublishing {
    publishToMavenCentral(SonatypeHost.DEFAULT)
    signAllPublications()
    configure(JavaLibrary(JavadocJar.Empty()))
  }
  pomFromGradleProperties()
}

afterEvaluate {
  signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
  }
  tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()
  }

  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions { jvmTarget = JavaVersion.VERSION_11.toString() }
  }
}

gradlePlugin {
  plugins {
    register("versioning") {
      id = "com.github.android-password-store.versioning-plugin"
      implementationClass = "versioning.VersioningPlugin"
    }
  }
}

dependencies {
  implementation(libs.build.agp)
  implementation(libs.build.dokka)
  implementation(libs.build.mavenpublish)
  implementation(libs.build.semver)
  implementation(libs.build.sentry)
}
