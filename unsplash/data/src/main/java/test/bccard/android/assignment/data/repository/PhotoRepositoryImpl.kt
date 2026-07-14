package test.bccard.android.assignment.data.repository

import test.bccard.android.assignment.core.domain.model.Photo
import test.bccard.android.assignment.data.mapper.toDomain
import test.bccard.android.assignment.data.remote.UnsplashApi
import test.bccard.android.assignment.domain.model.PhotoDetail
import test.bccard.android.assignment.domain.repository.PhotoRepository

class PhotoRepositoryImpl(private val api: UnsplashApi) : PhotoRepository {

    override suspend fun getPhotos(page: Int): Result<List<Photo>> =
        api.getPhotos(page = page)
            .map { dtos -> dtos.map { it.toDomain() } }

    override suspend fun getPhotoDetails(id: String): Result<PhotoDetail> =
        api.getPhoto(id).map { it.toDomain() }

    override suspend fun getDownloadUrl(id: String): Result<String?> =
        api.getDownloadLink(id).map { it.url }
}
