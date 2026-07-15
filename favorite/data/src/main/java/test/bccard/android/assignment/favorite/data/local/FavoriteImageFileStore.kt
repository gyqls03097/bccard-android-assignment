package test.bccard.android.assignment.favorite.data.local

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class FavoriteImageFileStore(context: Context) {

    private val dir = File(context.applicationContext.filesDir, "favorites")

    private fun fileOf(photoId: String) = File(dir, "$photoId.jpg")

    suspend fun save(photoId: String, bytes: ByteArray): Unit = withContext(Dispatchers.IO) {
        dir.mkdirs()
        fileOf(photoId).writeBytes(bytes)
    }

    suspend fun read(photoId: String): ByteArray? = withContext(Dispatchers.IO) {
        fileOf(photoId).takeIf { it.exists() }?.readBytes()
    }

    suspend fun delete(photoId: String): Unit = withContext(Dispatchers.IO) {
        fileOf(photoId).delete()
    }
}
