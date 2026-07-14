package test.bccard.android.assignment.favorite.domain.model

import test.bccard.android.assignment.core.model.Photo

data class FavoritePhoto(
    val photo: Photo,
    val createdAt: Long = 0L,
)
