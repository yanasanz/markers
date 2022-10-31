package com.example.maptest.dao

import androidx.room.*
import com.example.maptest.entity.PlacemarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacemarkDao {
    @Query("SELECT * FROM PlacemarkEntity ORDER BY id DESC")
    fun getAll(): Flow<List<PlacemarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: PlacemarkEntity)

    @Update
    suspend fun update(place: PlacemarkEntity)

    @Query("DELETE FROM PlacemarkEntity WHERE id = :id")
    suspend fun removeById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(placeList: List<PlacemarkEntity>)

}