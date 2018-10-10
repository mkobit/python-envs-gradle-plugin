package com.mkobit.environments.python.pyenv

import com.mkobit.gradle.test.kotlin.testkit.runner.setupProjectDir
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junitpioneer.jupiter.TempDirectory
import testsupport.newGradleRunner
import java.nio.file.Path

@ExtendWith(TempDirectory::class)
internal class PyenvEnvironmentsPluginTest {
  @Test
  internal fun `smoke test`(@TempDirectory.TempDir directory: Path) {
    val runner = newGradleRunner(directory) {
      setupProjectDir {
        "settings.gradle.kts"()
        "build.gradle.kts" {

        }
      }
    }
  }
}
