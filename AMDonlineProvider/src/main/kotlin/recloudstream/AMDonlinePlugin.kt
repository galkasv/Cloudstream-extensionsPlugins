package recloudstream

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class AMDonlinePlugin: Plugin() {
    override fun load(context: Context) {
        // Обов'язково реєструємо саме AMDonlineProvider
        registerMainProvider(AMDonlineProvider())
    }
}