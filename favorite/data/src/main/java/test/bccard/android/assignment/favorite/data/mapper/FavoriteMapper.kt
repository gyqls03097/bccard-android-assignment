package test.bccard.android.assignment.favorite.data.mapper

import test.bccard.android.assignment.core.domain.model.Photo
import test.bccard.android.assignment.core.domain.model.PhotoUser
import test.bccard.android.assignment.favorite.data.local.FavoriteEntity
import test.bccard.android.assignment.favorite.domain.model.FavoritePhoto

fun FavoriteEntity.toDomain(): FavoritePhoto = FavoritePhoto(
    photo = Photo(
        id = id,
        user = PhotoUser(
            id = userId,
            username = userUsername,
            name = userName,
            profileImageUrl = userProfileImageUrl,
        ),
        width = width,
        height = height,
        url = url,
        urlDetail = urlDetail,
    ),
    createdAt = createdAt,
)

fun Photo.toEntity(): FavoriteEntity = FavoriteEntity(
    id = id,
    url = url,
    urlDetail = urlDetail,
    width = width,
    height = height,
    userId = user?.id ?: "",
    userName = user?.name,
    userUsername = user?.username,
    userProfileImageUrl = user?.profileImageUrl,
    createdAt = System.currentTimeMillis(),
)
