// Use an integer for version numbers
version = 2

cloudstream {
    // All of these properties are optional, you can safely remove any of them.
    
    name = "AMD.online"
    description = "AMD.online - Донхуа та аніме"
    authors = listOf("galkasv")
    language = "uk"

    /**
     * Status int as one of the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta-only
     */
    status = 1 // Will be 3 if unspecified

    tvTypes = listOf(
        "Anime", 
        "AnimeMovie", 
        "Movie", 
        "TvSeries",
        "OVA")
    iconUrl = "https://www.google.com/s2/favicons?domain=amd.online&sz=%size%"

    isCrossPlatform = true
}