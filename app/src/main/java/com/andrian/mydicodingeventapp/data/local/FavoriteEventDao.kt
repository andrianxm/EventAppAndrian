package com.andrian.mydicodingeventapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(event: FavoriteEvent)

    @Delete
    suspend fun removeFavorite(event: FavoriteEvent)

    @Update
    fun updateFavorite(event: FavoriteEvent)

    @Query("SELECT * FROM favorite_events")
    fun getAllFavorites(): LiveData<List<FavoriteEvent>>

    @Query("SELECT * FROM favorite_events WHERE id = :id")
    suspend fun getFavoriteEventById(id: String): FavoriteEvent?

    @Query("SELECT EXISTS(SELECT * FROM favorite_events WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean
}
