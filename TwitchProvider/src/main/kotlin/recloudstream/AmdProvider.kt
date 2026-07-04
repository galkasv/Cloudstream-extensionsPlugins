package recloudstream

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.Jsoup

class AmdProvider : MainAPI() { 
    override var mainUrl = "https://amd.online" // Перевір, чи саме такий домен у браузері
    override var name = "AMD.online" 
    override var supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie) 
    override var hasMainPage = true 

    // 1. Пошук контенту на AMD.online
    override suspend fun search(query: String): List<SearchResponse> {
        // Тимчасовий тестовий лінк для пошуку (підкоригуємо під структуру сайту)
        val url = "$mainUrl/?s=$query" 
        val html = app.get(url).text
        val document = Jsoup.parse(html)

        // Тут будуть селектори конкретно для сайту AMD.online
        return document.select("article, div.poster").map { element ->
            val title = element.select("h2, a").text()
            val href = element.select("a").attr("href")
            val posterUrl = element.select("img").attr("src")

            newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = posterUrl
            }
        }
    }

    // 2. Сторінка тайтлу
    override suspend fun load(url: String): LoadResponse? {
        val html = app.get(url).text
        val document = Jsoup.parse(html)

        val title = document.select("h1").text()
        val poster = document.select("img.poster").attr("src")
        val description = document.select("div.description").text()

        val episodesList = mutableListOf<Episode>()
        
        // Базовий заглушковий варіант для однієї серії/плеєра
        episodesList.add(newEpisode(url) {
            this.name = "Дивитися на AMD.online"
        })

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = poster
            this.plot = description
            this.episodes = mutableMapOf(DubStatus.Subbed to episodesList)
        }
    }

    // 3. Пошук відео-посилань (іфреймів)
    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val html = app.get(data).text
        val document = Jsoup.parse(html)
        val iframes = document.select("iframe")
        var foundVideo = false

        for (iframe in iframes) {
            var iframeUrl = iframe.attr("src")
            if (iframeUrl.startsWith("//")) {
                iframeUrl = "https:$iframeUrl"
            }

            // Якщо вони теж використовують Kodik або інші стандартні плеєри:
            if (iframeUrl.contains("kodik") || iframeUrl.contains("ashdi") || iframeUrl.contains("anitube")) {
                loadExtractor(iframeUrl, data, subtitleCallback, callback)
                foundVideo = true
            }
        }
        return foundVideo
    }
}
