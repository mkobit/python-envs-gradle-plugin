package com.mkobit.environments.python.venv

import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.file.RegularFileProperty

open class VenvEnvironmentsExtension(
    val rootPython: RegularFileProperty,
    val environments: NamedDomainObjectCollection<VenvPythonEnvironment>
)
