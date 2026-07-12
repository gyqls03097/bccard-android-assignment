package test.bccard.android.assignment.comm

import androidx.compose.ui.graphics.ImageBitmap

internal class ImageCache {

    companion object {
        private const val MAX_SIZE = 20
    }

    private val lock = Any()
    private val items: MutableList<CacheData> = mutableListOf()

    fun save(id: String, bitmap: ImageBitmap) = synchronized(lock) {
        val index: Int = items.indexOfFirst { it.id == id }
        if (index >= 0) items.removeAt(index)
        items.add(0, CacheData(id, bitmap))
        if (MAX_SIZE < items.size) {
            items.removeAt(items.size - 1)
        }
    }

    fun get(id: String): ImageBitmap? = synchronized(lock) {
        val index: Int = items.indexOfFirst { it.id == id }
        if (index < 0) return@synchronized null
        val item = items.removeAt(index)
        items.add(0, item)
        item.bitmap
    }

    private class CacheData(val id: String, val bitmap: ImageBitmap)
}
