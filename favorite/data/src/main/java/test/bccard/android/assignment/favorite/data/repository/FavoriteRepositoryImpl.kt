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
import test.bccard.android.assignment.favorite.data.local.FavoriteImageFileStore
import test.bccard.android.assignment.favorite.data.mapper.toDomain
import test.bccard.android.assignment.favorite.data.mapper.toEntity
import test.bccard.android.assignment.favorite.domain.model.FavoritePhoto
import test.bccard.android.assignment.favorite.domain.repository.FavoriteRepository

class FavoriteRepositoryImpl(
    private val dao: FavoriteDao,
    private val imageStore: FavoriteImageFileStore,
    private val onImageDownload: suspend (url: String) -> ByteArray,
) : FavoriteRepository {

    companion object {
        fun create(
            context: Context,
            httpClient: HttpClient,
        ): FavoriteRepository {
            return FavoriteRepositoryImpl(
                dao = FavoriteDatabase.create(context).favoriteDao(),
                imageStore = FavoriteImageFileStore(context),
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

    override suspend fun removeFavorite(photo: Photo): Result<Boolean> = runCatching {
        if (dao.exists(photo.id)) {
            imageStore.delete(photo.id)
            dao.delete(photo.id)
            true
        } else {
            false
        }
    }

    override suspend fun addFavorite(photo: Photo, photoImageUrl: String): Result<Boolean> = runCatching {
        val bytes: ByteArray = onImageDownload(photoImageUrl)
        if (bytes.isNotEmpty()) {
            // 파일 먼저 저장: DB row 만 남고 이미지가 없는 고아 참조를 만들지 않기 위한 순서
            imageStore.save(photo.id, bytes)
            dao.update(photo.toEntity())
            true
        } else {
            false
        }
    }

    override suspend fun getImage(photoId: String): ByteArray? {
        return imageStore.read(photoId)
    }
}
