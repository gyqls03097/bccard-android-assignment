package test.bccard.android.assignment.domain.model

data class Photo(
    val id: String = "",
    val urls: PhotoUrls? = null,
    val user: PhotoUser? = null,
    val width: Int = 0,
    val height: Int = 0,
)

data class PhotoUrls(
    val raw: String? = null,
    val full: String? = null,
    val regular: String? = null,
    val small: String? = null,
    val thumb: String? = null,
)

data class PhotoUser(
    val id: String = "",
    val username: String? = null,
    val name: String? = null,
    val bio: String? = "",
    val profileImageUrl: String? = "",
)
