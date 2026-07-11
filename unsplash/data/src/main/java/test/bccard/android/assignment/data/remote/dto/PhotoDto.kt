package test.bccard.android.assignment.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDto(
    @SerialName("id") val id: String = "",
    @SerialName("urls") val urls: UrlsDto? = null,
    @SerialName("user") val user: UserDto? = null,
    @SerialName("width") val width: Int? = null,
    @SerialName("height") val height: Int? = null,
)

@Serializable
data class UrlsDto(
    @SerialName("raw") val raw: String? = null,
    @SerialName("full") val full: String? = null,
    @SerialName("regular") val regular: String? = null,
    @SerialName("small") val small: String? = null,
    @SerialName("thumb") val thumb: String? = null,
)

@Serializable
data class UserDto(
    @SerialName("id") val id: String = "",
    @SerialName("username") val username: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("bio") val bio: String? = null,
    @SerialName("profile_image") val profileImage: ProfileImageDto? = null,
)

@Serializable
data class ProfileImageDto(
    @SerialName("small") val small: String? = null,
    @SerialName("medium") val medium: String? = null,
    @SerialName("large") val large: String? = null,
)
