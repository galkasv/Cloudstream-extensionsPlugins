// Use an integer for version numbers
version = 1

cloudstream {
    // All of these properties are optional, you can safely remove any of them.
    
    name = "Animesss"
    description = "Animesss - Дивитися аніме та донхуа"
    authors = listOf("galkasv") // Впиши свій нік
    language = "uk" // Саме цей рядок додасть український прапор на TV!

    /**
     * Status int as one of the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta-only
     **/
    status = 1 // Will be 3 if unspecified

    tvTypes = listOf(
        "Anime", 
        "AnimeMovie", 
        "Movie", 
        "TvSeries",
        "OVA")
    iconUrl = "https://www.google.com/s2/favicons?domain=animesss.com&sz=%size%"

    isCrossPlatform = true
}