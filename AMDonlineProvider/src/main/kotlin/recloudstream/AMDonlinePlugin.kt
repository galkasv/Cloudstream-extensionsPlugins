package recloudstream

import android.content.Context
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.plugins.*

@CloudstreamPlugin
class AMDonlinePlugin : Plugin() {
    override fun load(context: Context) {
        registerMainProvider(AMDonlineProvider())
    }
}