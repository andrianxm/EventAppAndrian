package com.andrian.mydicodingeventapp.data.response.retrofit

import com.andrian.mydicodingeventapp.data.response.DetailEventResponse
import com.andrian.mydicodingeventapp.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") isActive: Int,
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getEventById(
        @Path("id") id: String,
    ): Call<DetailEventResponse>

    @GET("events")
    fun searchEvents(
        @Query("active") active: Int,
        @Query("q") query: String,
    ): Call<EventResponse>

}