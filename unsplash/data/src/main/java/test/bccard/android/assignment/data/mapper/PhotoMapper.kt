package test.bccard.android.assignment.data.mapper

import test.bccard.android.assignment.core.model.Photo
import test.bccard.android.assignment.core.model.PhotoUser
import test.bccard.android.assignment.data.remote.dto.PhotoDetailDto
import test.bccard.android.assignment.data.remote.dto.PhotoDetailExifDto
import test.bccard.android.assignment.data.remote.dto.PhotoDetailLocationDto
import test.bccard.android.assignment.data.remote.dto.PhotoDto
import test.bccard.android.assignment.data.remote.dto.UserDto
import test.bccard.android.assignment.domain.model.PhotoDetail
import test.bccard.android.assignment.domain.model.PhotoExif
import test.bccard.android.assignment.domain.model.PhotoLocation

fun PhotoDto.toDomain(): Photo = Photo(
    id = id,
    user = user?.toDomain(),
    width = width ?: 0,
    height = height ?: 0,
    url = urls?.let { it.regular ?: it.small ?: it.full ?: it.thumb ?: it.raw },
    urlDetail = urls?.let { it.raw ?: it.full ?: it.regular ?: it.small ?: it.thumb },
)

fun PhotoDetailDto.toDomain(): PhotoDetail = PhotoDetail(
    views = views ?: 0,
    downloads = downloads ?: 0,
    exif = exif?.toDomain(),
    location = location?.toDomain(),
    tags = tags?.mapNotNull { it.title } ?: emptyList(),
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
