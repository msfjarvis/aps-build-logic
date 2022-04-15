@file:Suppress("PropertyName")

import io.sentry.android.gradle.InstrumentationFeature

plugins {
  id("com.android.application")
  id("io.sentry.android.gradle")
}

sentry {
  autoUploadProguardMapping.set(true)
  ignoredBuildTypes.set(setOf("debug"))
  tracingInstrumentation {
    enabled.set(true)
    features.set(setOf(InstrumentationFeature.FILE_IO))
  }
}
