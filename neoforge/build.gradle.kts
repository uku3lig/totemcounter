plugins {
    id("multiloader-platform")
    id("net.neoforged.moddev")
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