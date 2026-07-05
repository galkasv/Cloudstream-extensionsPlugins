package recloudstream

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.Jsoup

import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin

@CloudstreamPlugin
class AMDonlineProvider : MainAPI() {
    override var name = "AMD.online"
    override var mainUrl = "https://amd.online"
    override var supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie, TvType.Movie, TvType.TvSeries, TvType.OVA)
    
    // Вимикаємо головну сторінку, щоб додаток не видавав помилку "Not implemented".
    // Тепер джерело працюватиме на 100% через рядок пошуку!
    override var hasMainPage = false 

    // 1. Пошук контенту на AMD.online (всеїдний та гнучкий варіант)
    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/?s=$query" 
        val html = app.get(url).text
        val document = Jsoup.parse(html)

        // Скануємо картки аніме за всіма типовими класами кіносайтів
        return document.select("article, div.poster, div.movie-item, div.shortstory, a.movie-pack, .th-item").mapNotNull { element ->
            val titleElement = element.select("h2, h3, a.title, div.title, .th-title").firstOrNull() ?: element
            val title = titleElement.text().trim()
            
            if (title.isEmpty()) return@mapNotNull null

            val href = element.select("a").attr("href")
            if (href.isEmpty()) return@mapNotNull null

            // Розумний збір картинок (враховує Lazy-load сайту)
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

        val title = document.select("h1, .film-title, .entry-title").text().trim()
        val poster = document.select("img.poster, div.poster img, .film-poster img, .poster-img img").attr("src")
        val description = document.select("div.description, div.story, .film-description, #fstory").text().trim()

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
