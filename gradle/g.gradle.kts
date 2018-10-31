import javax.inject.Inject

plugins {
  base
}

tasks {
  named("wrapper", Wrapper::class) {
    gradleVersion = "5.0-milestone-1"
  }
}

//objectFactory: ObjectFactory
open class Producer @Inject constructor(objectFactory: ObjectFactory) : DefaultTask() {
  @get:OutputFile
  val destination: RegularFileProperty = objectFactory.fileProperty()

  @TaskAction
  fun doThing() {
    destination.get().asFile.writeText("text")
  }
}

open class ConfigurableConsumer @Inject constructor(objectFactory: ObjectFactory) : DefaultTask() {
  @get:InputFile
  val data: RegularFileProperty = objectFactory.fileProperty()
  @TaskAction
  fun doThing() {
    println("Configurable: ${data.get().asFile.readText()}")
  }
}

open class ArgsConsumer @Inject constructor(
    @get:InputFile val data: Provider<RegularFile>
) : DefaultTask() {
  @TaskAction
  fun doThing() {
    println("Args: ${data.get().asFile.readText()}")
  }
}

open class ArgsHackConsumer @Inject constructor(
    private val actualData: Provider<RegularFile>
) : DefaultTask() {
    @get:InputFile val data: Provider<RegularFile> = project.provider { }.flatMap { actualData }
  @TaskAction
  fun doThing() {

  }
}

val producer by tasks.creating(Producer::class) {
  destination.set(layout.buildDirectory.file("myfile.txt"))
}

tasks.create<ConfigurableConsumer>("configurableConsumer") {
  data.set(producer.destination)
}
tasks.create<ArgsConsumer>("argsConsumer", producer.destination)
tasks.create<ArgsHackConsumer>("argsHackConsumer", producer.destination)
