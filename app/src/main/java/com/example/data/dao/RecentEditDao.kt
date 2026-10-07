package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.RecentEditEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentEditDao {
    @Query("SELECT * FROM recent_edits ORDER BY timestamp DESC")
    fun getAllRecentEdits(): Flow<List<RecentEditEntity>>

    @Query("SELECT * FROM recent_edits WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteEdits(): Flow<List<RecentEditEntity>>

    @Query("SELECT * FROM recent_edits WHERE id = :id LIMIT 1")
    suspend fun getEditById(id: String): RecentEditEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEdit(edit: RecentEditEntity)

    @Update
    suspend fun updateEdit(edit: RecentEditEntity)

    @Delete
    suspend fun deleteEdit(edit: RecentEditEntity)

    @Query("UPDATE recent_edits SET isFavorite = :isFav WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFav: Boolean)

    @Query("DELETE FROM recent_edits WHERE id = :id")
    suspend fun deleteById(id: String)
}
