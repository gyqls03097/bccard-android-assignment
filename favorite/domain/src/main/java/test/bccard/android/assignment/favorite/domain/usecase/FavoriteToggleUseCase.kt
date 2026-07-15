package test.bccard.android.assignment.favorite.domain.usecase

import test.bccard.android.assignment.core.domain.model.Photo
import test.bccard.android.assignment.favorite.domain.repository.FavoriteRepository

class FavoriteToggleUseCase(
    private val repository: FavoriteRepository,
    private val getDownloadUrl: suspend (photoId: String) -> String?,
) {

    suspend operator fun invoke(
        photo: Photo,
    ): Result<Boolean> {
        if (repository.isFavorite(photo.id)) {
            return repository.removeFavorite(photo)
        }
        return runCatching {
            val url: String? = getDownloadUrl(photo.id)
            if (url == null) {
                false
            } else {
                repository.addFavorite(photo, url).getOrThrow()
            }
        }
    }
}
