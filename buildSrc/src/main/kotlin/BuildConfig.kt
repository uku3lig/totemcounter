object BuildConfig {
    const val MINECRAFT_VERSION: String = "26.2-rc-2"
    const val FABRIC_LOADER_VERSION: String = "0.19.3"
    const val NEOFORGE_VERSION: String = "26.2.0-alpha.0+rc-2.20260614.052634"
    const val FABRIC_API_VERSION: String = "0.152.0+26.2"
    const val UKULIB_VERSION: String = "2.0.0+26.2-rc-2-build.330"

    const val MOD_VERSION: String = "1.12.1"

    const val MODRINTH_PROJECT_ID: String = "T9R7YTnA"

    fun createVersionString(): String {
        return "$MOD_VERSION+mc$MINECRAFT_VERSION"
    }
}