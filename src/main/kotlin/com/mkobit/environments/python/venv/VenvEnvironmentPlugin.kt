package com.mkobit.environments.python.venv

import com.mkobit.environments.python.PythonEnvironmentsExtension
import com.mkobit.environments.python.PythonEnvironmentsPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.container
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.the

open class VenvEnvironmentPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.run {
      apply<PythonEnvironmentsPlugin>()

      val environments = the(PythonEnvironmentsExtension::class)

      val venvEnvironmentContainer = project.container<VenvPythonEnvironment>()
      val venvExtension = (environments as ExtensionAware).extensions.create(
          "venv",
          VenvEnvironmentsExtension::class,
          layout.fileProperty(),
          venvEnvironmentContainer
      )
    }
  }
}
