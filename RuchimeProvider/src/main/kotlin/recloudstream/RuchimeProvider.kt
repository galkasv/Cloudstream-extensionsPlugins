package recloudstream.ruchime

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin

@CloudstreamPlugin
class RuchimeProvider : MainAPI() {
    override var name = "Ruchime"
    override var mainUrl = "https://1.ruchime.org"
    override var supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie, TvType.Movie, TvType.TvSeries, TvType.OVA)
    override var hasMainPage = true

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val html = app.get(mainUrl).text
        val document = Jsoup.parse(html)
        
        val items = document.select("div.th-item").mapNotNull { element ->
            val title = element.select("div.th-title").text().trim()
            if (title.isEmpty()) return@mapNotNull null
            val href = element.select("a").attr("href")
            val posterUrl = element.select("img").attr("src")
            
            newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = posterUrl
            }
        }
        return newHomePageResponse("Новинки Ruchime", items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/?s=$query"
        val html = app.get(url).text
        val document = Jsoup.parse(html)

        return document.select("div.th-item").map { element ->
            val title = element.select("div.th-title").text()
            val href = element.select("a").attr("href")
            val posterUrl = element.select("img").attr("src")

            newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = posterUrl
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val html = app.get(url).text
        val document = Jsoup.parse(html)
        
        val title = document.select("h1").text()
        val poster = document.select("div.poster img").attr("src")
        val description = document.select("div.description").text()
        
        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = poster
            this.plot = description
        }
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        val html = app.get(data).text
        val document = Jsoup.parse(html)
        val iframes = document.select("iframe")
        var foundVideo = false

        for (iframe in iframes) {
            val iframeUrl = iframe.attr("src")
            if (iframeUrl.contains("kodik") || iframeUrl.contains("vk.com") || iframeUrl.contains("allvideo")) {
                loadExtractor(iframeUrl, data, subtitleCallback, callback)
                foundVideo = true
            }
        }
        return foundVideo
    }
}