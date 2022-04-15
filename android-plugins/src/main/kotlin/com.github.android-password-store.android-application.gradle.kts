/*
 * Copyright © 2014-2021 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */
@file:Suppress("UnstableApiUsage")

import signing.configureBuildSigning

plugins {
  id("com.android.application")
  id("com.github.android-password-store.android-common")
}

android {
  val minifySwitch = project.providers.environmentVariable("DISABLE_MINIFY")

  adbOptions.installOptions("--user 0")

  dependenciesInfo {
    includeInBundle = false
    includeInApk = false
  }

  buildFeatures {
    viewBinding = true
    buildConfig = true
  }

  buildTypes {
    named("release") {
      isMinifyEnabled = !minifySwitch.isPresent
      setProguardFiles(
        listOf(
          "proguard-android-optimize.txt",
          "proguard-rules.pro",
          "proguard-rules-missing-classes.pro",
        )
      )
    }
    named("debug") {
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
      isMinifyEnabled = false
    }
  }

  project.configureBuildSigning()
}
