import org.ajoberstar.grgit.Grgit
import org.cadixdev.gradle.licenser.LicenseExtension

plugins {
  id("org.cadixdev.licenser") version "0.6.1" apply false
  id("org.ajoberstar.grgit") version "4.1.0" apply false
  jacoco
}

allprojects {
  group = "org.ammonium"
  version = "0.1-SNAPSHOT"
}

subprojects {
  apply(plugin = "java-library")
  apply(plugin = "maven-publish")
  apply(plugin = "checkstyle")
  apply(plugin = "org.cadixdev.licenser")

  repositories {
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
  }

  configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
  }

  configure<CheckstyleExtension> {
    configFile = rootProject.file(".checkstyle/checkstyle.xml")
    toolVersion = "8.41"
  }

  configure<LicenseExtension> {
    include("**/*.java")
    include("**/*.kts")

    setHeader(rootProject.file("HEADER.txt"))
    newLine(true)
  }

  tasks.named<JavaCompile>("compileJava") {
    options.compilerArgs.addAll(
      listOf(
        "-parameters"
      )
    )
  }

  // Compile option: -DgitCommitHash
  if (!project.hasProperty("gitCommitHash")) {
    apply(plugin = "org.ajoberstar.grgit")
    ext["gitCommitHash"] = try {
      extensions.getByName<Grgit>("grgit").head()?.abbreviatedId
    } catch (e: Exception) {
      logger.warn("Error getting commit hash", e)

      "no.git.id"
    }
  }
}

