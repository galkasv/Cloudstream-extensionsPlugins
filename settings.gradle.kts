rootProject.name = "CloudstreamPlugins"

// Список папок, які ми хочемо повністю ігнорувати при збірці
val ignoredPlugins = listOf(
    "DailymotionProvider",
    "InternetArchiveProvider",
    "InvidiousProvider"
)

File(rootDir, ".").eachDir { dir ->
    if (!ignoredPlugins.contains(dir.name) && File(dir, "build.gradle.kts").exists()) {
        include(dir.name)
    }
}

fun File.eachDir(block: (File) -> Unit) {
    listFiles()?.filter { it.isDirectory }?.forEach { block(it) }
}
