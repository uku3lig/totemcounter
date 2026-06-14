plugins {
    id("multiloader-platform")
    id("net.neoforged.moddev")
}

repositories {
    maven {
        name = "Maven for PR #3198" // https://github.com/neoforged/NeoForge/pull/3198
        url = uri("https://prmaven.neoforged.net/NeoForge/pr3198")
        content {
            includeModule("net.neoforged", "neoforge")
            includeModule("net.neoforged", "testframework")
        }
    }
}

neoForge {
    version = BuildConfig.NEOFORGE_VERSION

    runs {
        create("client") {
            client()
            gameDirectory = rootProject.file("run")
        }
    }

    mods {
        create(rootProject.name) {
            sourceSet(sourceSets["main"])
            sourceSet(project(":common").sourceSets["main"])
        }
    }
}

dependencies {
    implementation("net.uku3lig:ukulib-neoforge:${BuildConfig.UKULIB_VERSION}")
}