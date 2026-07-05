rootProject.name = "CloudstreamPlugins"

val ignoredPlugins = listOf<String>()

File(rootDir, ".").eachDir { dir ->
    if (!ignoredPlugins.contains(dir.name) && File(dir, "build.gradle.kts").exists()) {
        include(dir.name)
    }
}

fun File.eachDir(block: (File) -> Unit) {
    listFiles()?.filter { it.isDirectory }?.forEach { block(it) }
}
