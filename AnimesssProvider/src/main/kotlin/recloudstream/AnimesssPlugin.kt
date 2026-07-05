package recloudstream

import android.content.Context
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.plugins.*

@CloudstreamPlugin
class AnimesssPlugin : Plugin() {
    override fun load(context: Context) {
        registerMainProvider(AnimesssProvider())
    }
}