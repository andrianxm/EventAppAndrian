package com.andrian.mydicodingeventapp.data

import androidx.datastore.core.IOException
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.andrian.mydicodingeventapp.data.local.FavoriteEvent
import com.andrian.mydicodingeventapp.data.local.FavoriteEventDao
import com.andrian.mydicodingeventapp.data.response.DetailEventResponse
import com.andrian.mydicodingeventapp.data.response.EventResponse
import com.andrian.mydicodingeventapp.data.response.ListEventsItem
import com.andrian.mydicodingeventapp.data.response.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await

class Repository(
    private val apiService: ApiService,
    private val favoriteEventDao: FavoriteEventDao,
) {

    fun getDetailEvent(eventId: String): LiveData<Result<DetailEventResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.getEventById(eventId).await()
                emit(Result.Success(response))
            } catch (e: IOException) {
                emit(Result.Error("Network error: ${e.message}"))
            } catch (e: Exception) {
                emit(Result.Error("Error: ${e.message}"))
            }
        }

    suspend fun saveFavorite(favorite: FavoriteEvent) {
        withContext(Dispatchers.IO) {
            favoriteEventDao.addFavorite(favorite)
        }
    }

    suspend fun delete(favorite: FavoriteEvent) {
        withContext(Dispatchers.IO) {
            favoriteEventDao.removeFavorite(favorite)
        }
    }

    suspend fun isFavorite(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            favoriteEventDao.isFavorite(id)
        }
    }

    suspend fun searchEvents(active: Int, query: String): Result<EventResponse> {
        return try {
            val response = apiService.searchEvents(active, query).await() // Tambahkan .await()
            Result.Success(response) // Kembalikan hasil sukses jika berhasil
        } catch (e: IOException) {
            Result.Error("Network error: ${e.message}") // Tangani error jaringan
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}") // Tangani error umum
        }
    }

    suspend fun getEvents(isActive: Int): Result<List<ListEventsItem>> {
        return try {
            val response = apiService.getEvents(isActive).await()
            Result.Success(response.listEvents)
        } catch (e: IOException) {
            Result.Error("Network error: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    suspend fun getFinishedEvents(): Result<List<ListEventsItem>> {
        return try {
            val response = apiService.getEvents(0).await()
            Result.Success(response.listEvents)
        } catch (e: IOException) {
            Result.Error("Network error: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    suspend fun getUpcomingEvents(): Result<List<ListEventsItem>> {
        return try {
            val response = apiService.getEvents(1).await()
            Result.Success(response.listEvents)
        } catch (e: IOException) {
            Result.Error("Network error: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    companion object {

        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteEventDao: FavoriteEventDao,
        ): Repository = instance ?: synchronized(this) {
            instance ?: Repository(apiService, favoriteEventDao)
        }.also { instance = it }
    }
}