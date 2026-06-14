plugins {
    id("multiloader-base")
    id("java-library")

    id("net.fabricmc.fabric-loom")
    id("io.freefair.lombok") version "9.5.0"
}

dependencies {
    minecraft("com.mojang:minecraft:${BuildConfig.MINECRAFT_VERSION}")

    compileOnly("net.uku3lig:ukulib-common:${BuildConfig.UKULIB_VERSION}")

    // provided both by fabric and neoforge
    compileOnly("net.fabricmc:sponge-mixin:0.17.3+mixin.0.8.7")
    compileOnly("io.github.llamalad7:mixinextras-common:0.5.4")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.4")
}

tasks.jar { enabled = false }