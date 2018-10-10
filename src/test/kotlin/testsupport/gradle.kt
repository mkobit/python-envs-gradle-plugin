package testsupport

import com.mkobit.gradle.test.kotlin.testkit.runner.projectDirPath
import org.gradle.testkit.runner.GradleRunner
import java.nio.file.Path

internal fun newGradleRunner(projectDirectory: Path, configuration: GradleRunner.() -> Unit = {}): GradleRunner =
    GradleRunner.create().apply {
      withPluginClasspath()
      projectDirPath = projectDirectory
      configuration()
    }
