package test.bccard.android.assignment

import android.content.Context
import test.bccard.android.assignment.data.Pref
import test.bccard.android.assignment.data.remote.UnsplashApi
import test.bccard.android.assignment.data.remote.UnsplashHttpClient
import test.bccard.android.assignment.data.repository.PhotoRepositoryImpl
import test.bccard.android.assignment.domain.repository.PhotoRepository
import test.bccard.android.assignment.domain.usecase.GetPhotosUseCase

class AppDI(context: Context) {

    val pref: Pref = Pref(context)

    private val httpClient = UnsplashHttpClient.create { pref.accessKey }

    private val photoRepository: PhotoRepository = PhotoRepositoryImpl(UnsplashApi(httpClient))

    val getPhotos = GetPhotosUseCase(photoRepository)
}