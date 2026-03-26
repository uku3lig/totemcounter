object BuildConfig {
    const val MINECRAFT_VERSION: String = "26.1"
    const val FABRIC_LOADER_VERSION: String = "0.18.4"
    const val NEOFORGE_VERSION: String = "26.1.0.1-beta"
    const val FABRIC_API_VERSION: String = "0.144.3+26.1"
    const val UKULIB_VERSION: String = "2.0.0+26.1"

    const val MOD_VERSION: String = "1.12.0"

    const val MODRINTH_PROJECT_ID: String = "T9R7YTnA"

    fun createVersionString(): String {
        return "$MOD_VERSION+mc$MINECRAFT_VERSION"
    }
}