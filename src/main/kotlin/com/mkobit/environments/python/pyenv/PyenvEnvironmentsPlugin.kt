package com.mkobit.environments.python.pyenv

import com.mkobit.environments.python.PythonEnvironment
import com.mkobit.environments.python.PythonEnvironmentsExtension
import com.mkobit.environments.python.PythonEnvironmentsPlugin
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.reflect.TypeOf
import org.gradle.kotlin.dsl.*

class PyenvEnvironmentsPlugin : Plugin<Project> {

  companion object {
    private const val PYENV_TASK_GROUP = "pyenv"
  }

  override fun apply(target: Project) {
    target.run {
      apply<BasePlugin>()
      apply<PythonEnvironmentsPlugin>()
      val environments = the<PythonEnvironmentsExtension>()
      val containerType = object : TypeOf<NamedDomainObjectContainer<PyenvPythonEnvironment>>() {}
//      val container = container<PyenvPythonEnvironment> {
//
//        PyenvPythonEnvironment(it, )
//      }
    }
  }
}
