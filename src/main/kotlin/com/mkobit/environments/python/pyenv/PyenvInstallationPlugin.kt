package com.mkobit.environments.python.pyenv

import com.mkobit.environments.python.PythonEnvironmentsExtension
import com.mkobit.environments.python.PythonEnvironmentsPlugin
import com.mkobit.environments.python.extensions
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.reflect.TypeOf
import org.gradle.kotlin.dsl.*
import javax.inject.Inject

class PyenvInstallationPlugin @Inject constructor(
    private val objectFactory: ObjectFactory,
    private val projectLayout: ProjectLayout
) : Plugin<Project> {

  companion object {
    private const val PYENV_TASK_GROUP = "pyenv"
  }

  override fun apply(target: Project) {
    target.run {
      apply<BasePlugin>()
      apply<PythonEnvironmentsPlugin>()
      val environments = the<PythonEnvironmentsExtension>()
      val containerType = object : TypeOf<NamedDomainObjectContainer<PyenvInstallation>>() {}
      val container = container { name ->
        val pyenvVersion = objectFactory.property<String>()
        val downloadPyenvInstallerBundle = tasks.register<DownloadPyenvBundle>("downloadPyenvInstalledBundle$name") {
          group = PYENV_TASK_GROUP
          // TODO: support non-github downloads for self-hosting
          distributionUrl.set(pyenvVersion.map { "https://github.com/pyenv/pyenv/archive/$it.tar.gz" })
          // TODO: isolate pyenv stuff into own directory
          destination.set(layout.buildDirectory.file(pyenvVersion.map { "pyenv-$it.tar.gz" }))
        }

        val decompressPyenvInstallerBundle = tasks.register<DecompressPyenvBundle>("installPyenv$name") {
          group = PYENV_TASK_GROUP
          bundle.set(downloadPyenvInstallerBundle.get().destination)
          // TODO: isolate pyenv stuff into its own directory
          destination.set(layout.buildDirectory.dir(pyenvVersion.map { "pyenv-$it" }))
        }

        // 'bin/pyenv' (at least in recent versions) symlinks to the
        // Unfortunately Gradle doesn't seem to preserve the symlink from unpacking
        // https://github.com/gradle/gradle-native/issues/294
        // TODO: use flatMap from gradle 5.0 to not force configuration
        val pyenv = decompressPyenvInstallerBundle.map { install -> install.destination.file("libexec/pyenv") }.get()
        PyenvInstallation(
            name,
            pyenv,
            pyenvVersion
        )
      }
      environments.extensions.add(containerType, "pyenv", container)
    }
  }
}
