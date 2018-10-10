package com.mkobit.environments.python.pyenv

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.file.RelativePath
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

// TODO: make cacheable
open class DecompressPyenvBundle @Inject constructor(
    projectLayout: ProjectLayout,
    objectFactory: ObjectFactory
) : DefaultTask() {
  // TODO: make these constructor args instead of user configurable
  @get:InputFile
  val bundle: RegularFileProperty = projectLayout.fileProperty()

  // TODO: properly handle up-to-date checking when output directories is figured out
  @get:OutputDirectory
  val destination: DirectoryProperty = projectLayout.directoryProperty()

  @TaskAction
  fun decompressPyenvBundle() {
    project.sync {
      // TODO: bunch of assumptions about gzip and tar here
      from(project.tarTree(project.resources.gzip(bundle.get().asFile)))
      // Where does this stupid thing come from?!?
      // Doesn't show up when using 'tar' CLI.
      // Seems like maybe it comes from 'git archive'? - https://github.com/backdrop-ops/contrib/issues/55
      exclude("pax_global_header")
      eachFile copyDetails@ {
        // strip leading version part to avoid the double nesting
        path = path.substring(relativePath.segments[0].length)
      }
      includeEmptyDirs = true
      into(destination.get().asFile)
    }
  }
}
