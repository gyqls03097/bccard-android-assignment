package test.bccard.android.assignment.favorite.data.repository

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import test.bccard.android.assignment.core.domain.model.Photo
import test.bccard.android.assignment.favorite.data.local.FavoriteDao
import test.bccard.android.assignment.favorite.data.local.FavoriteDatabase
import test.bccard.android.assignment.favorite.data.local.FavoriteImageEntity
import test.bccard.android.assignment.favorite.data.mapper.toDomain
import test.bccard.android.assignment.favorite.data.mapper.toEntity
import test.bccard.android.assignment.favorite.domain.model.FavoritePhoto
import test.bccard.android.assignment.favorite.domain.repository.FavoriteRepository

class FavoriteRepositoryImpl(
    private val dao: FavoriteDao,
    private val onImageDownload: suspend (url: String) -> ByteArray,
) : FavoriteRepository {

    companion object {
        fun create(
            context: Context,
            httpClient: HttpClient,
        ): FavoriteRepository {
            return FavoriteRepositoryImpl(
                dao = FavoriteDatabase.create(context).favoriteDao(),
                onImageDownload = { url -> httpClient.get(url).body() }
            )
        }
    }

    override fun findAll(): Flow<List<FavoritePhoto>> {
        return dao
            .findAll()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override fun findIdAll(): Flow<Set<String>> {
        return dao
            .findIdAll()
            .map { it.toSet() }
    }

    override suspend fun isFavorite(photoId: String): Boolean {
        return runCatching { dao.exists(photoId) }.getOrNull() ?: false
    }

    override suspend fun toggleFavorite(photo: Photo): Result<Boolean> = runCatching {
        if (dao.exists(photo.id)) {
            dao.deleteImage(photo.id)
            dao.delete(photo.id)
            true
        } else {
            val url = photo.url
            if (url == null) {
                false
            } else {
                dao.update(photo.toEntity())
                val bytes = onImageDownload(url)
                if (bytes.isNotEmpty()) {
                    dao.updateImage(FavoriteImageEntity(photoId = photo.id, bytes = bytes))
                }
                true
            }
        }
    }

    override suspend fun getImage(photoId: String): ByteArray? {
        return dao.findImageById(photoId)
    }
}
