package com.example.dicodingevent.data.remote.retrofit
import com.example.dicodingevent.data.remote.response.EventDetailResponse
import com.example.dicodingevent.data.remote.response.EventListResponse
import retrofit2.http.*

interface ApiService {
    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int,
        @Query("limit") limit: Int,
    ): EventListResponse

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int,
        @Query("q") query: String,
    ): EventListResponse

    @GET("events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: Int,
    ): EventDetailResponse
}