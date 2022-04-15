/*
 * Copyright © 2014-2021 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

package flavors

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.language.nativeplatform.internal.BuildType

/**
 * When the "slimTests" project property is provided, disable the unit test tasks on `release` build
 * type to avoid running the same tests repeatedly in different build
 * variants.
 *
 * Examples: `./gradlew test -PslimTests` will run unit tests for `debug` build variants
 * in Android App and Library projects, and all tests in JVM projects.
 */
internal fun Project.configureSlimTests() {
  if (providers.gradleProperty(SLIM_TESTS_PROPERTY).isPresent) {
    // disable unit test tasks on the release build type for Android Library projects
    extensions.findByType<LibraryAndroidComponentsExtension>()?.run {
      beforeVariants(selector().withBuildType(BuildType.RELEASE.name)) {
        it.enableUnitTest = false
        it.enableAndroidTest = false
      }
    }

    // disable unit test tasks on the release build type and free flavor for Android Application
    // projects.
    extensions.findByType<ApplicationAndroidComponentsExtension>()?.run {
      beforeVariants(selector().withBuildType(BuildType.RELEASE.name)) {
        it.enableUnitTest = false
        it.enableAndroidTest = false
      }
    }
  }
}

private const val SLIM_TESTS_PROPERTY = "slimTests"
