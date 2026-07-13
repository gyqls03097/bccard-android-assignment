package test.bccard.android.assignment.domain.usecase

import test.bccard.android.assignment.core.model.Photo
import test.bccard.android.assignment.domain.repository.PhotoRepository

class GetPhotosUseCase(private val repository: PhotoRepository) {

    suspend operator fun invoke(page: Int): Result<List<Photo>> {
        val safePage = page.coerceAtLeast(1)
        return repository.getPhotos(page = safePage)
    }
}
