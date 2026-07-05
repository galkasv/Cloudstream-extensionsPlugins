package recloudstream

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin

@CloudstreamPlugin
class AnimesssPlugin: CloudstreamPlugin() {
    override fun load(context: Context) {
        registerMainAPI(AnimesssProvider())
    }
}