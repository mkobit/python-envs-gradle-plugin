import buildsrc.DependencyInfo
import buildsrc.ProjectInfo
import com.gradle.publish.PluginConfig
import com.gradle.publish.PublishPlugin
import org.gradle.api.internal.HasConvention
import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream
import java.net.URL

plugins {
  id("com.gradle.build-scan") version "1.16"
  `kotlin-dsl`
  `java-library`
  `java-gradle-plugin`
  id("com.gradle.plugin-publish") version "0.10.0"
  id("com.github.ben-manes.versions") version "0.20.0"
  id("org.jetbrains.dokka") version "0.9.17"
  // Only used for local publishing for testing
  `maven-publish`
}

version = "0.1.0"
group = "com.mkobit.environments.python"
description = ""

val gitCommitSha: String by lazy {
  ByteArrayOutputStream().use {
    project.exec {
      commandLine("git", "rev-parse", "HEAD")
      standardOutput = it
    }
    it.toString(Charsets.UTF_8.name()).trim()
  }
}

val SourceSet.kotlin: SourceDirectorySet
  get() = withConvention(KotlinSourceSet::class) { kotlin }

buildScan {
  fun env(key: String): String? = System.getenv(key)

  setTermsOfServiceAgree("yes")
  setTermsOfServiceUrl("https://gradle.com/terms-of-service")

  // Env variables from https://circleci.com/docs/2.0/env-vars/
  if (env("CI") != null) {
    logger.lifecycle("Running in CI environment, setting build scan attributes.")
    tag("CI")
    env("CIRCLE_BRANCH")?.let { tag(it) }
    env("CIRCLE_BUILD_NUM")?.let { value("Circle CI Build Number", it) }
    env("CIRCLE_BUILD_URL")?.let { link("Build URL", it) }
    env("CIRCLE_SHA1")?.let { value("Revision", it) }
    //    Issue with Circle CI/Gradle with caret (^) in URLs
//    see: https://discuss.gradle.org/t/build-scan-plugin-1-10-3-issue-when-using-a-url-with-a-caret/24965
//    see: https://discuss.circleci.com/t/circle-compare-url-does-not-url-escape-caret/18464
//    env("CIRCLE_COMPARE_URL")?.let { link("Diff", it) }
    env("CIRCLE_REPOSITORY_URL")?.let { value("Repository", it) }
    env("CIRCLE_PR_NUMBER")?.let { value("Pull Request Number", it) }
    link("Repository", ProjectInfo.projectUrl)
  }
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
  jcenter()
}

dependencies {
  api(gradleApi())
  testImplementation(kotlin("reflect"))
  testImplementation(DependencyInfo.gradleTestKotlinExtensions)
  testImplementation("com.mkobit.gradle.test:assertj-gradle:0.2.0")
  testImplementation(DependencyInfo.assertk)
  testImplementation(DependencyInfo.junitPioneer)
  testImplementation(DependencyInfo.mockito)
  testImplementation(DependencyInfo.mockitoKotlin)
  DependencyInfo.junitTestImplementationArtifacts.forEach {
    testImplementation(it)
  }
  DependencyInfo.junitTestRuntimeOnlyArtifacts.forEach {
    testRuntimeOnly(it)
  }
}

tasks {
  val wrapper by registering(Wrapper::class) {
    gradleVersion = "4.10-rc-2"
  }

  withType<Jar>().configureEach {
    from(project.projectDir) {
      include("LICENSE.txt")
      into("META-INF")
    }
    manifest {
      attributes(mapOf(
        "Build-Revision" to gitCommitSha,
        "Implementation-Version" to project.version
      ))
    }
  }

  withType<Javadoc>().configureEach {
    options {
      header = project.name
      encoding = "UTF-8"
    }
  }

  withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
  }

  "test"(Test::class) {
    useJUnitPlatform()
    systemProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager")
    testLogging {
      events("skipped", "failed")
    }
  }

  val main by sourceSets
  val sourcesJar by registering(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles a JAR of the source code"
    classifier = "sources"
    from(main.allSource)
  }

  // No Java code, so don't need the javadoc task.
  // Dokka generates our documentation.
  remove(getByName("javadoc"))
  val dokka by existing(DokkaTask::class) {
    dependsOn(main.classesTaskName)
    jdkVersion = 8
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
    sourceDirs = main.kotlin.srcDirs
    // See https://github.com/Kotlin/dokka/issues/196
    externalDocumentationLink(delegateClosureOf<DokkaConfiguration.ExternalDocumentationLink.Builder> {
      url = URL("https://docs.gradle.org/${wrapper.get().gradleVersion}/javadoc/")
    })
    externalDocumentationLink(delegateClosureOf<DokkaConfiguration.ExternalDocumentationLink.Builder> {
      url = URL("https://docs.oracle.com/javase/8/docs/api/")
    })
  }

  val javadocJar by registering(Jar::class) {
    dependsOn(dokka)
    description = "Assembles a JAR of the generated Javadoc"
    from(dokka.get().outputDirectory)
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    classifier = "javadoc"
  }

  val assemble by existing {
    dependsOn(sourcesJar, javadocJar)
  }
}

artifacts {
  val sourcesJar by tasks.getting
  val javadocJar by tasks.getting
  add("archives", sourcesJar)
  add("archives", javadocJar)
}

gradlePlugin {
  // Don't get the extensions for NamedDomainObjectContainer here because we only have a NamedDomainObjectContainer
  // See https://github.com/gradle/kotlin-dsl/issues/459
  plugins {
    register("venv") {
      id = "com.mkobit.environments.python.venv"
      implementationClass = "com.mkobit.environments.python.venv.VenvEnvironmentPlugin"
    }
  }
}

pluginBundle {
  vcsUrl = ProjectInfo.projectUrl
  description = ""
  tags = listOf()
  website = ProjectInfo.projectUrl

  plugins(delegateClosureOf<NamedDomainObjectContainer<PluginConfig>> {
    invoke {
    }
  })
}
