package test.bccard.android.assignment.domain.usecase

import test.bccard.android.assignment.domain.model.PhotoDetail
import test.bccard.android.assignment.domain.repository.PhotoRepository

class GetPhotoDetailUseCase(private val repository: PhotoRepository) {

    suspend operator fun invoke(id: String): Result<PhotoDetail> {
        return repository.getPhotoDetails(id)
    }
}
