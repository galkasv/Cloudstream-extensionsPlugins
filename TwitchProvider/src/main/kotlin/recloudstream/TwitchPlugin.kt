package recloudstream

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class TwitchPlugin: Plugin() {
    override fun load(context: Context) {
        // Реєструємо тільки наш провайдер (який тепер AMD.online)
        registerMainAPI(TwitchProvider())
    }
}
