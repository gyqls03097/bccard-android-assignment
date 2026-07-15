package test.bccard.android.assignment.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDetailDto(
    @SerialName("views") val views: Int? = null,
    @SerialName("downloads") val downloads: Int? = null,
    @SerialName("exif") val exif: PhotoDetailExifDto? = null,
    @SerialName("location") val location: PhotoDetailLocationDto? = null,
    @SerialName("tags") val tags: List<PhotoDetailTagDto>? = null,
)

@Serializable
data class PhotoDetailExifDto(
    @SerialName("make") val make: String? = null,
    @SerialName("model") val model: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("exposure_time") val exposureTime: String? = null,
    @SerialName("aperture") val aperture: String? = null,
    @SerialName("focal_length") val focalLength: String? = null,
    @SerialName("iso") val iso: Int? = null,
)

@Serializable
data class PhotoDetailLocationDto(
    @SerialName("name") val name: String? = null,
    @SerialName("city") val city: String? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("position") val position: PhotoDetailPositionDto? = null,
)

@Serializable
data class PhotoDetailPositionDto(
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null,
)

@Serializable
data class PhotoDetailTagDto(
    @SerialName("type") val type: String? = null,
    @SerialName("title") val title: String? = null,
)
