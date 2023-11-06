import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.9.20"
  application
}

repositories {
  gradlePluginPortal()
  mavenCentral()
}

val one: SourceSet by sourceSets.creating {
  java.srcDir("src/one/java")
  kotlin.srcDir("src/one/kotlin")
  kotlin.destinationDirectory = java.destinationDirectory
}

dependencies {
  implementation("com.google.guava:guava:32.1.1-jre")
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  implementation(one.output)

  sourceSets.named("one") {
    implementation("org.apache.commons:commons-lang3:3.12.0")
  }
}

tasks.named<Test>("test") {
  useJUnitPlatform()
}
tasks.withType<Test>().configureEach {
  maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
}

tasks.compileKotlin {
  destinationDirectory = tasks.compileJava.get().destinationDirectory
}

tasks.compileTestKotlin {
  destinationDirectory = tasks.compileTestJava.get().destinationDirectory
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "20"
    allWarningsAsErrors = true
  }
}

java {
  sourceCompatibility = JavaVersion.VERSION_20
  targetCompatibility = JavaVersion.VERSION_20
  modularity.inferModulePath.set(true)
}

application {
  mainClass.set("fr.tawane.main.AppKt")
}

tasks.compileJava {
  options.compilerArgumentProviders.add(
    CommandLineArgumentProvider {
      listOf("--patch-module", "fr.tawane.main=${sourceSets["main"].output.asPath}")
    },
  )
}

tasks.jar {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
