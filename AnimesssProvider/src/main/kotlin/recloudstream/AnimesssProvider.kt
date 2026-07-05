package recloudstream

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.Jsoup

import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin

@CloudstreamPlugin
class AnimesssProvider : MainAPI() {
    override var name = "Animesss"
    override var mainUrl = "https://animesss.com"
    override var supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie, TvType.Movie, TvType.TvSeries, TvType.OVA) 
    override var hasMainPage = false 

    // 1. Пошук аніме
    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/?s=$query"
        val html = app.get(url).text
        val document = Jsoup.parse(html)

        return document.select("article.post, div.anime-item, div.poster-card").map { element ->
            val title = element.select("h2.title, a.title, h3").text()
            val href = element.select("a").attr("href")
            val posterUrl = element.select("img").attr("data-src").ifEmpty {
                element.select("img").attr("src")
            }

            newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = posterUrl
            }
        }
    }

    // 2. Сторінка тайтлу та формування серій
    override suspend fun load(url: String): LoadResponse? {
        val html = app.get(url).text
        val document = Jsoup.parse(html)

        val title = document.select("h1.entry-title, h1.title").text()
        val poster = document.select("div.poster img, .anime-poster img, .single-poster img").attr("src")
        val description = document.select("div.description, .entry-content, .video-description").text()

        val episodesList = mutableListOf<Episode>()
        val episodeElements = document.select("ul.episodes-list li, div.series-list a, .episodes-nav a")
        
        if (episodeElements.isNotEmpty()) {
            episodeElements.forEachIndexed { index, element ->
                val epHref = element.attr("href").ifEmpty { url }
                val epName = element.text().ifEmpty { "Серія ${index + 1}" }
                
                episodesList.add(newEpisode(epHref) {
                    this.name = epName
                    this.episode = index + 1
                })
            }
        } else {
            episodesList.add(newEpisode(url) {
                this.name = "Дивитися аніме"
            })
        }

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = poster
            this.plot = description
            this.episodes = mutableMapOf(DubStatus.Subbed to episodesList)
        }
    }

    // 3. Відео з плеєра Kodik
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
                iframeUrl.contains("aniqit")) {
                
                if (iframeUrl.contains("ls.kodikres.com")) continue

                loadExtractor(iframeUrl, data, subtitleCallback, callback)
                foundVideo = true
            }
        }
        return foundVideo
    }
}