package test.bccard.android.assignment.domain.usecase

import test.bccard.android.assignment.domain.repository.PhotoRepository

class GetPhotoDownloadUrlUseCase(private val repository: PhotoRepository) {

    suspend operator fun invoke(id: String): Result<String?> = repository.getDownloadUrl(id)
}
