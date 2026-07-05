package recloudstream

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class AnimesssPlugin: Plugin() {
    override fun load(context: Context) {
        // Замість registerMainProvider тепер використовується registerProvider
        registerProvider(AnimesssProvider()) 
    }
}