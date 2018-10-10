package com.mkobit.environments.python

import org.gradle.api.plugins.ExtensionAware

open class PythonEnvironmentsExtension

// TODO: has to be a better way to do this
internal val PythonEnvironmentsExtension.extensions get() = (this as ExtensionAware).extensions
