plugins {
    id("com.android.library")
    kotlin("android")
    id("com.lagradost.cloudstream3.gradle")
}

cloudstream {
    this.name = "AMD.online"
    this.description = "AMD.online - Серіали та аніме в українському дубляжі"
    this.authors = listOf("galkasv")
    this.language = "uk"
    this.version = 2
    this.status = 1

    this.tvTypes = listOf(
        "Anime", 
        "AnimeMovie", 
        "Movie", 
        "TvSeries",
        "OVA"
    )
    this.iconUrl = "https://www.google.com/s2/favicons?domain=amd.online&sz=%size%"
    this.isCrossPlatform = true
}