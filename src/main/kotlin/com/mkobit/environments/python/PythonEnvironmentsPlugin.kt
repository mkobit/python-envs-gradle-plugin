package com.mkobit.environments.python

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

open class PythonEnvironmentsPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.run {
      extensions.create("pythonEnvironments", PythonEnvironmentsExtension::class)
    }
  }
}
