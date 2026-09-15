plugins {
    id("java-library")
    id("idea")
}

group = "net.uku3lig"
version = BuildConfig.createVersionString()

base {
    archivesName = rootProject.name + "-" + project.name
}

java.toolchain.languageVersion = JavaLanguageVersion.of(25)

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(25)
}

tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}

tasks.jar {
    destinationDirectory.set(file(rootProject.layout.buildDirectory).resolve("libs"))
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.uku3lig.net/releases") }
}
