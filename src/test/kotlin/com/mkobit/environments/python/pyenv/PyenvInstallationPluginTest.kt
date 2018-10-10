package com.mkobit.environments.python.pyenv

import assertk.assert
import assertk.assertions.contains
import com.mkobit.gradle.test.kotlin.testkit.runner.build
import com.mkobit.gradle.test.kotlin.testkit.runner.setupProjectDir
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junitpioneer.jupiter.TempDirectory
import testsupport.newGradleRunner
import java.nio.file.Path

// TODO: test everything with deferred configuration (it doesn't work right now)
@ExtendWith(TempDirectory::class)
internal class PyenvInstallationPluginTest {
  @Test
  internal fun `can install a single a pyenv distribution`(@TempDirectory.TempDir directory: Path) {
    val runner = newGradleRunner(directory) {
      setupProjectDir {
        "build.gradle.kts" {
          content = """
            plugins {
              id("com.mkobit.environments.python.pyenv.installation")
            }

            val SinglePyenv by pythonEnvironments.pyenv.creating {
              version.set("v1.2.7")
            }

            task("checkPyenvInstallation") {
              dependsOn(SinglePyenv.pyenvBinary)
              doFirst("execute pyenv") {
                exec {
                  executable(SinglePyenv.pyenvBinary.get().asFile)
                  args("--version")
                }
              }
            }

          """.trimIndent().toByteArray()
        }
        "settings.gradle.kts"()
      }
    }

    val result = runner.build("checkPyenvInstallation")
    assert(result.output).contains("pyenv 1.2.7")
  }

  @Test
  internal fun `can install multiple pyenv distributions`(@TempDirectory.TempDir directory: Path) {
  }
}
