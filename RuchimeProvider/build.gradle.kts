plugins {
    id("com.android.library")
    kotlin("android")
    id("com.lagradost.cloudstream3.gradle")
}

version = "1" // Версія плагіна

cloudstream {
    description = "Ruchime - Дивитися аніме та донхуа"
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
    iconUrl = "https://www.google.com/s2/favicons?domain=1.ruchime.org&sz=%size%"
    isCrossPlatform = true
}