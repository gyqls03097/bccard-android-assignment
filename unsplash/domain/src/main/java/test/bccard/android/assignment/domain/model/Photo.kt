package test.bccard.android.assignment.domain.model

data class Photo(
    val id: String = "",
    val user: PhotoUser? = null,
    val width: Int = 0,
    val height: Int = 0,
    val url: String? = null,
)

data class PhotoUser(
    val id: String = "",
    val username: String? = null,
    val name: String? = null,
    val bio: String? = "",
    val profileImageUrl: String? = "",
)
