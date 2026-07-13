package test.bccard.android.assignment.data

import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import test.bccard.android.assignment.core.model.Photo
import test.bccard.android.assignment.data.remote.UnsplashApi
import test.bccard.android.assignment.data.remote.UnsplashHttpClient
import test.bccard.android.assignment.data.repository.PhotoRepositoryImpl

class PhotoRepositoryNetworkTest {

    private lateinit var client: HttpClient
    private lateinit var repository: PhotoRepositoryImpl

    @Before
    fun setUp() {
        val clientId = ""  // FIXME 개별 아이디 입력 필요
        client = UnsplashHttpClient.create({ clientId })
        repository = PhotoRepositoryImpl(UnsplashApi(client))
    }

    @After
    fun tearDown() {
        client.close()
    }

    @Test
    fun testApis() = runBlocking {
        val photosRes: Result<List<Photo>> = repository.getPhotos(page = 1)
        assertTrue("/photos 성공여부: ${photosRes.exceptionOrNull()}", photosRes.isSuccess)

        val photos: List<Photo> = photosRes.getOrThrow()
        assertEquals("10 개를 호출했음", 10, photos.size)

        val first: Photo = photos.first()
        val itemId: String = first.id
        assertTrue("id 존재 여부 확인", itemId.isNotBlank())

        val photoDetailRes = repository.getPhotoDetails(itemId)
        assertTrue("/photos/{id} 성공여부: ${photoDetailRes.exceptionOrNull()}", photoDetailRes.isSuccess)

        val downloadRes = repository.getDownloadUrl(itemId)
        assertTrue("/photos/{id}/download 성공여부: ${downloadRes.exceptionOrNull()}", downloadRes.isSuccess)

        val download: String? = downloadRes.getOrThrow()
        assertTrue("다운로드 URL 확인", download?.startsWith("http") == true)
    }
}
