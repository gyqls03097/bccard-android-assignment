package test.bccard.android.assignment.favorite.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        FavoriteEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        fun create(context: Context): FavoriteDatabase =
            Room.databaseBuilder(context.applicationContext, FavoriteDatabase::class.java, "favorite.db")
                .build()
    }
}
