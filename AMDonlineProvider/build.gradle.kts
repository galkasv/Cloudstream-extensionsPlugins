plugins {
    id("com.android.library")
    kotlin("android")
    id("com.lagradost.cloudstream3.gradle")
}

version = "3" // Версія плагіна 

cloudstream {
    description = "AMD.online - Серіали, донхуа та аніме"
    authors = listOf("galkasv")
    language = "uk"
    status = 1

    tvTypes = listOf(
        "Anime", 
        "AnimeMovie", 
        "Movie", 
        "TvSeries",
        "OVA"
    )
    iconUrl = "https://www.google.com/s2/favicons?domain=amd.online&sz=%size%"
    isCrossPlatform = true
}