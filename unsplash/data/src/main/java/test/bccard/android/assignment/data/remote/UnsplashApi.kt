package test.bccard.android.assignment.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import test.bccard.android.assignment.data.remote.dto.DownloadLinkDto
import test.bccard.android.assignment.data.remote.dto.PhotoDetailDto
import test.bccard.android.assignment.data.remote.dto.PhotoDto

class UnsplashApi(
    private val client: HttpClient,
    private val baseUrl: String = UnsplashHttpClient.BASE_URL,
) {

    suspend fun getPhotos(page: Int): Result<List<PhotoDto>> = runCatchingApi {
        client.get("$baseUrl/photos") {
            parameter("page", page)
        }.body()
    }

    suspend fun getPhoto(id: String): Result<PhotoDetailDto> = runCatchingApi {
        client.get("$baseUrl/photos/$id").body()
    }

    suspend fun getDownloadLink(id: String): Result<DownloadLinkDto> = runCatchingApi {
        client.get("$baseUrl/photos/$id/download").body()
    }

    private inline fun <T> runCatchingApi(block: () -> T): Result<T> = try {
        Result.success(block())
    } catch (throwable: Throwable) {
        Result.failure(throwable)
    }
}
