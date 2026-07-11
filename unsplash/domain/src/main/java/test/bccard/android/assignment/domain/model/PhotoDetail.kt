package test.bccard.android.assignment.domain.model

data class PhotoDetail(
    val views: Int = 0,
    val downloads: Int = 0,
    val exif: PhotoExif? = null,
    val location: PhotoLocation? = null,
    val tags: List<String> = emptyList(),
)

data class PhotoExif(
    val make: String? = null,
    val model: String? = null,
    val name: String? = null,
    val exposureTime: String? = null,
    val aperture: String? = null,
    val focalLength: String? = null,
    val iso: Int? = null,
)

data class PhotoLocation(
    val name: String? = null,
    val city: String? = null,
    val country: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
