package com.mkobit.environments.python.pyenv

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

// TODO: make cacheable
open class DownloadPyenvBundle @Inject constructor(
    projectLayout: ProjectLayout,
    objectFactory: ObjectFactory
) : DefaultTask() {
  // TODO: make these constructor args instead of user configurable
  @get:Input
  val distributionUrl: Property<String> = objectFactory.property()

  @get:OutputFile
  val destination: RegularFileProperty = projectLayout.fileProperty()

  @TaskAction
  fun downloadPyenv() {
    ant.invokeMethod(
        "get",
        mapOf(
            "src" to distributionUrl.get(),
            "dest" to destination.get().asFile
        )
    )
  }
}
