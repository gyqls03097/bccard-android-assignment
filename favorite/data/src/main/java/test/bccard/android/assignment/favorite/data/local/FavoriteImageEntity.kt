package test.bccard.android.assignment.favorite.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_image")
class FavoriteImageEntity(
    @PrimaryKey
    @ColumnInfo(name = "photoId") val photoId: String,

    @ColumnInfo(name = "bytes") val bytes: ByteArray,
)
