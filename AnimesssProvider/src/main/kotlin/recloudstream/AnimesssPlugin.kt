package recloudstream

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class AnimesssPlugin: Plugin() {
    override fun load(context: Context) {
        // Обов'язково реєструємо саме AnimesssProvider
        registerMainProvider(AnimesssProvider())
    }
}