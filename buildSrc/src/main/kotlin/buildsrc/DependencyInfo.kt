package buildsrc

@Suppress("UNUSED", "MemberVisibilityCanBePrivate")
object DependencyInfo {
  const val junitPlatformVersion = "1.3.0"
  const val junitJupiterVersion = "5.3.0"
  const val junit5Log4jVersion = "2.11.1"

  const val assertk = "com.willowtreeapps.assertk:assertk-jvm:0.12"
  const val gradleTestKotlinExtensions = "com.mkobit.gradle.test:gradle-test-kotlin-extensions:0.6.0"
  const val mockito = "org.mockito:mockito-core:2.21.0"
  const val mockitoKotlin = "com.nhaarman:mockito-kotlin:1.6.0"
  const val junitPioneer = "org.junit-pioneer:junit-pioneer:0.1.2"
  val junitPlatformRunner = junitPlatform("runner")
  val junitJupiterApi = junitJupiter("api")
  val junitJupiterEngine = junitJupiter("engine")
  val junitJupiterParams = junitJupiter("params")
  val log4jCore = log4j("core")
  val log4jJul = log4j("jul")

  val junitTestImplementationArtifacts = listOf(
      junitPlatformRunner,
      junitJupiterApi,
      junitJupiterParams
  )

  val junitTestRuntimeOnlyArtifacts = listOf(
    junitJupiterEngine,
    log4jCore,
    log4jJul
  )

  fun junitJupiter(module: String) = "org.junit.jupiter:junit-jupiter-$module:$junitJupiterVersion"
  fun junitPlatform(module: String) = "org.junit.platform:junit-platform-$module:$junitPlatformVersion"
  fun log4j(module: String) = "org.apache.logging.log4j:log4j-$module:$junit5Log4jVersion"
}
