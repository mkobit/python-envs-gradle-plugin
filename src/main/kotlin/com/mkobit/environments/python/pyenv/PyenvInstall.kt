package com.mkobit.environments.python.pyenv

import org.gradle.api.DefaultTask
import org.gradle.api.file.*
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

open class PyenvInstall @Inject constructor(
    projectLayout: ProjectLayout,
    objectFactory: ObjectFactory
) : DefaultTask() {

  @get:InputFile
  val pyenv: RegularFileProperty = projectLayout.fileProperty()

  @get:InputFile
  val pyenvRoot: DirectoryProperty = projectLayout.directoryProperty()

  @get:Input
  val pythonVersion: Property<String> = objectFactory.property()

  @get:Console
  val verbose: Property<Boolean> = objectFactory.property()


  // TODO: output of python binary and home
  @get:OutputDirectory
  val pythonHome: Provider<Directory> = pyenvRoot.dir(pythonVersion.map { "versions/$it" })

  @get:OutputFile
  val python: Provider<RegularFile> = pythonHome.map { it.file("bin/python")}

  @TaskAction
  fun installPyenv() {
    project.exec {
      executable(pyenv.get().asFile)
      environment("PYENV_ROOT", pyenvRoot.get().asFile)
      args("--skip-existing")
      if (verbose.getOrElse(false)) {
        args("--verbose")
      }
      args(pythonVersion.get())
    }
  }
}
