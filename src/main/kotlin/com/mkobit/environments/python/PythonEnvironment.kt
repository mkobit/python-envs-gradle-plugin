package com.mkobit.environments.python

import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

interface PythonEnvironment {
  val pythonHome: Provider<Directory>
  val python: Provider<RegularFile>
}
