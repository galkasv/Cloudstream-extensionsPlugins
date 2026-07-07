plugins {
    id("com.android.library")
    kotlin("android")
    id("com.lagradost.cloudstream3.gradle")
}

version = "3" // Версія плагіна

cloudstream {
    description = "Animesss - Дивитися аніме та донхуа"
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
    iconUrl = "https://www.google.com/s2/favicons?domain=animesss.com&sz=%size%"
    isCrossPlatform = true
}