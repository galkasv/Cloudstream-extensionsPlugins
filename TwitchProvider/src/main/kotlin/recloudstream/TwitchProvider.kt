package recloudstream

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.Jsoup

// Назву класу залишаємо TwitchProvider, щоб працював TwitchPlugin.kt
class TwitchProvider : MainAPI() { 
    override var mainUrl = "https://amd.online" 
    override var name = "AMD.online" // А в додатку назва буде красивою: AMD.online!
    override var supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie) 
    override var hasMainPage = true 

    // 1. Пошук контенту на AMD.online
    override suspend fun search(query: String): List<SearchResponse> {
        // Зазвичай пошук на таких сайтах іде через параметр ?s= або /search/
        val url = "$mainUrl/?s=$query" 
        val html = app.get(url).text
        val document = Jsoup.parse(html)

        // Шукаємо картки аніме. Додано типові класи українських кіносайтів
        return document.select("article, div.poster, div.movie-item, div.shortstory, a.movie-pack").mapNotNull { element ->
            // Назва та посилання
            val titleElement = element.select("h2, h3, a.title, div.title, .th-title").firstOrNull() ?: element
            val title = titleElement.text().trim()
            
            // Якщо назви немає, пропускаємо цей елемент
            if (title.isEmpty()) return@mapNotNull null

            // Шукаємо лінк на сторінку аніме
            val href = element.select("a").attr("href")
            if (href.isEmpty()) return@mapNotNull null

            // Розумний пошук постера (перебираємо всі можливі атрибути лінивого завантаження картинок)
            val img = element.select("img").firstOrNull()
            val posterUrl = img?.let {
                it.attr("data-src").ifEmpty {
                    it.attr("data-lazy-src").ifEmpty {
                        it.attr("src")
                    }
                }
            } ?: ""

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
        val poster = document.select("img.poster, div.poster img").attr("src")
        val description = document.select("div.description, div.story").text()

        val episodesList = mutableListOf<Episode>()
        
        episodesList.add(newEpisode(url) {
            this.name = "Дивитися на AMD.online"
        })

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = poster
            this.plot = description
            this.episodes = mutableMapOf(DubStatus.Subbed to episodesList)
        }
    }

    // 3. Пошук відео-посилань (плеєрів)
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

            if (iframeUrl.contains("kodik") || 
                iframeUrl.contains("tomion.org") || 
                iframeUrl.contains("kodikplayer.com") || 
                iframeUrl.contains("aniqit") ||
                iframeUrl.contains("ashdi")) {
                
                if (iframeUrl.contains("ls.kodikres.com")) continue

                loadExtractor(iframeUrl, data, subtitleCallback, callback)
                foundVideo = true
            }
        }
        return foundVideo
    }
}
