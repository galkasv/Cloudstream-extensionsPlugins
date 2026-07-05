package recloudstream

import android.content.Context

@com.lagradost.cloudstream3.plugins.CloudstreamPlugin
class AMDonlinePlugin : com.lagradost.cloudstream3.plugins.Plugin() {
    override fun load(context: Context) {
        registerMainProvider(AMDonlineProvider())
    }
}