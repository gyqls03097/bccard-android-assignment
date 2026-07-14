package test.bccard.android.assignment

import android.content.Context
import test.bccard.android.assignment.core.domain.UrlDownloaderImpl
import test.bccard.android.assignment.core.domain.repository.UrlDownloader
import test.bccard.android.assignment.core.remote.DefaultHttpClient
import test.bccard.android.assignment.data.Pref
import test.bccard.android.assignment.data.remote.UnsplashApi
import test.bccard.android.assignment.data.repository.PhotoRepositoryImpl
import test.bccard.android.assignment.domain.repository.PhotoRepository
import test.bccard.android.assignment.domain.usecase.DownloadPhotoUseCase
import test.bccard.android.assignment.domain.usecase.GetPhotoDetailUseCase
import test.bccard.android.assignment.domain.usecase.GetPhotosUseCase
import test.bccard.android.assignment.favorite.data.repository.FavoriteRepositoryImpl
import test.bccard.android.assignment.favorite.domain.repository.FavoriteRepository

class AppDI(context: Context) {

    val pref: Pref = Pref(context)

    private val httpClient = DefaultHttpClient.create()

    private val photoRepository: PhotoRepository = PhotoRepositoryImpl(UnsplashApi(httpClient, pref::accessKey))

    val favoriteRepository: FavoriteRepository = FavoriteRepositoryImpl.create(context, httpClient)

    val urlDownloader: UrlDownloader = UrlDownloaderImpl(context)

    val getPhotos = GetPhotosUseCase(photoRepository)

    val getPhotoDetail = GetPhotoDetailUseCase(photoRepository)

    val downloadPhoto = DownloadPhotoUseCase(photoRepository, urlDownloader)

}
