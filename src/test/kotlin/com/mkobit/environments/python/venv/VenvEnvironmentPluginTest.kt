package com.mkobit.environments.python.venv

import com.mkobit.gradle.test.kotlin.testkit.runner.build
import com.mkobit.gradle.test.kotlin.testkit.runner.projectDirPath
import com.mkobit.gradle.test.kotlin.testkit.runner.setupProjectDir
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junitpioneer.jupiter.TempDirectory
import java.nio.file.Path

@ExtendWith(TempDirectory::class)
internal class VenvEnvironmentPluginTest {

  @Test
  internal fun `can access nested extension when plugin applied`(@TempDirectory.TempDir directory: Path) {
    GradleRunner.create().apply {
      withPluginClasspath()
      projectDirPath = directory
      setupProjectDir {
        "build.gradle.kts" {
          content = """
            plugins {
              id("com.mkobit.environments.python.venv")
            }

            pythonEnvironments {
              venv {
              }
            }
          """.trimIndent().toByteArray()
        }
      }
    }.build("help")
  }
}
