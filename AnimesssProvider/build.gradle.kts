cloudstream {
    this.name = "Animesss"
    this.description = "Animesss - Дивитися аніме та донхуа"
    this.authors = listOf("galkasv")
    this.language = "uk" // Додає український прапор

    // Перенесли всередину блоку і захистили від конфліктів за допомогою this.
    this.version = 1 

    /**
     * Status int as one of the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta-only
     **/
    this.status = 1 

    this.tvTypes = listOf(
        "Anime", 
        "AnimeMovie", 
        "Movie", 
        "TvSeries",
        "OVA"
    )
    
    // Автоматично підтягне гарну іконку сайту з фавікону Google
    this.iconUrl = "https://www.google.com/s2/favicons?domain=animesss.com&sz=%size%"

    this.isCrossPlatform = true
}