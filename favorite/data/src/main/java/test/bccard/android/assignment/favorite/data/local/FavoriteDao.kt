package test.bccard.android.assignment.favorite.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite ORDER BY createdAt DESC")
    fun findAll(): Flow<List<FavoriteEntity>>

    @Query("SELECT id FROM favorite")
    fun findIdAll(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite WHERE id = :photoId)")
    suspend fun exists(photoId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE id = :photoId")
    suspend fun delete(photoId: String)

    @Query("SELECT * FROM favorite WHERE id = :photoId")
    suspend fun findById(photoId: String): FavoriteEntity?
}
