package com.mkobit.environments.python.pyenv

import com.mkobit.environments.python.PythonEnvironment
import org.gradle.api.Named
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

class PyenvPythonEnvironment(
    private val name: String,
    override val pythonHome: Provider<Directory>,
    override val python: Provider<RegularFile>
) : Named, PythonEnvironment {
  override fun getName(): String = name
}
