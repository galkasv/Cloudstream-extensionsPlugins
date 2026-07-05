plugins {
    id("com.android.library")
    kotlin("android")
    id("com.lagradost.cloudstream3.gradle")
}

cloudstream {
    this.name = "Animesss"
    this.description = "Animesss - Дивитися аніме та донхуа"
    this.authors = listOf("galkasv")
    this.language = "uk"
    this.version = 1
    this.status = 1

    this.tvTypes = listOf(
        "Anime", 
        "AnimeMovie", 
        "Movie", 
        "TvSeries",
        "OVA"
    )
    this.iconUrl = "https://www.google.com/s2/favicons?domain=animesss.com&sz=%size%"
    this.isCrossPlatform = true
}