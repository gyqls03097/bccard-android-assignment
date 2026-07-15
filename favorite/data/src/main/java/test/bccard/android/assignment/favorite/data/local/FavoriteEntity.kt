package test.bccard.android.assignment.favorite.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,

    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "urlDetail") val urlDetail: String?,
    @ColumnInfo(name = "width") val width: Int,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "userName") val userName: String?,
    @ColumnInfo(name = "userUsername") val userUsername: String?,
    @ColumnInfo(name = "userProfileImageUrl") val userProfileImageUrl: String?,
    @ColumnInfo(name = "createdAt") val createdAt: Long,
)
