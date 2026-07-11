package test.bccard.android.assignment.comm

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get

internal class ImageLoaders {

    companion object {
        const val TIMEOUT_MS = 10_000L
        val instance: ImageLoaders = ImageLoaders()
    }

    val cache = ImageCache()

    private val client = HttpClient(OkHttp) {
        expectSuccess = true
        install(HttpTimeout) {
            requestTimeoutMillis = TIMEOUT_MS
            connectTimeoutMillis = TIMEOUT_MS
            socketTimeoutMillis = TIMEOUT_MS
        }
    }

    fun peek(url: String): ImageBitmap? = if (url.isBlank()) null else cache.get(url)

    suspend fun load(url: String): ImageBitmap? {
        if (url.isBlank()) return null
        cache.get(url)?.let { return it }
        val loaded = fetch(url) ?: return null
        cache.save(url, loaded)
        return loaded
    }

    suspend fun fetch(url: String): ImageBitmap? {
        if (url.isBlank()) return null
        return runCatching {
            val bytes: ByteArray = client.get(url).body()
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
        }.getOrNull()
    }
}
