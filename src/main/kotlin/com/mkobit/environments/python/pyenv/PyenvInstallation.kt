package com.mkobit.environments.python.pyenv

import org.gradle.api.Named
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

class PyenvInstallation(
    private val name: String,
    val pyenvBinary: Provider<RegularFile>,
    val version: Property<String>
) : Named {
  override fun getName(): String = name
}
