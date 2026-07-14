package test.bccard.android.assignment.domain.usecase

import test.bccard.android.assignment.core.domain.repository.UrlDownloader
import test.bccard.android.assignment.domain.repository.PhotoRepository

class DownloadPhotoUseCase(
    private val repository: PhotoRepository,
    private val downloader: UrlDownloader,
) {

    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.getDownloadUrl(id)
            .map { url: String? -> url?.let { downloader.download(url, "$id.jpg") } }
    }
}
