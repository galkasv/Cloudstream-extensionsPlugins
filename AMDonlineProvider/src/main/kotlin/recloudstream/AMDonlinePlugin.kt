package recloudstream

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin

@CloudstreamPlugin
class AMDonlinePlugin: CloudstreamPlugin() {
    override fun load(context: Context) {
        // Реєструємо наш провайдер у системі Cloudstream
        registerMainAPI(AMDonlineProvider())
    }
}