package test.bccard.android.assignment.domain.repository

import test.bccard.android.assignment.core.domain.model.Photo
import test.bccard.android.assignment.domain.model.PhotoDetail

interface PhotoRepository {

    suspend fun getPhotos(page: Int): Result<List<Photo>>

    suspend fun getPhotoDetails(id: String): Result<PhotoDetail>

    suspend fun getDownloadUrl(id: String): Result<String?>
}
