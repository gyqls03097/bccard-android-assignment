package test.bccard.android.assignment.favorite.domain.repository

import kotlinx.coroutines.flow.Flow
import test.bccard.android.assignment.core.domain.model.Photo
import test.bccard.android.assignment.favorite.domain.model.FavoritePhoto

interface FavoriteRepository {

    fun findAll(): Flow<List<FavoritePhoto>>

    fun findIdAll(): Flow<Set<String>>

    suspend fun isFavorite(photoId: String): Boolean

    suspend fun toggleFavorite(photo: Photo): Result<Boolean>

    suspend fun getImage(photoId: String): ByteArray?
}
