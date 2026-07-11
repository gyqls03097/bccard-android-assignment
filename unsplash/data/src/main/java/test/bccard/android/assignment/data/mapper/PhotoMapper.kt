package test.bccard.android.assignment.data.mapper

import test.bccard.android.assignment.data.remote.dto.PhotoDetailDto
import test.bccard.android.assignment.data.remote.dto.PhotoDetailExifDto
import test.bccard.android.assignment.data.remote.dto.PhotoDetailLocationDto
import test.bccard.android.assignment.data.remote.dto.PhotoDto
import test.bccard.android.assignment.data.remote.dto.UrlsDto
import test.bccard.android.assignment.data.remote.dto.UserDto
import test.bccard.android.assignment.domain.model.Photo
import test.bccard.android.assignment.domain.model.PhotoDetail
import test.bccard.android.assignment.domain.model.PhotoExif
import test.bccard.android.assignment.domain.model.PhotoLocation
import test.bccard.android.assignment.domain.model.PhotoUrls
import test.bccard.android.assignment.domain.model.PhotoUser

fun PhotoDto.toDomain(): Photo = Photo(
    id = id,
    urls = urls?.toDomain(),
    user = user?.toDomain(),
    width = width ?: 0,
    height = height ?: 0,
)

fun PhotoDetailDto.toDomain(): PhotoDetail = PhotoDetail(
    views = views ?: 0,
    downloads = downloads ?: 0,
    exif = exif?.toDomain(),
    location = location?.toDomain(),
    tags = tags?.mapNotNull { it.title } ?: emptyList(),
)

fun UrlsDto.toDomain(): PhotoUrls = PhotoUrls(
    raw = raw,
    full = full,
    regular = regular,
    small = small,
    thumb = thumb,
)

fun UserDto.toDomain(): PhotoUser = PhotoUser(
    id = id,
    username = username,
    name = name,
    bio = bio,
    profileImageUrl = profileImage?.medium ?: profileImage?.large ?: profileImage?.small,
)

fun PhotoDetailExifDto.toDomain(): PhotoExif = PhotoExif(
    make = make,
    model = model,
    name = name,
    exposureTime = exposureTime,
    aperture = aperture,
    focalLength = focalLength,
    iso = iso,
)

fun PhotoDetailLocationDto.toDomain(): PhotoLocation = PhotoLocation(
    name = name,
    city = city,
    country = country,
    latitude = position?.latitude,
    longitude = position?.longitude,
)
