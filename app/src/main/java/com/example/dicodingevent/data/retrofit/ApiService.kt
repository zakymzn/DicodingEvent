package com.example.dicodingevent.data.retrofit
import com.example.dicodingevent.data.response.EventDetailResponse
import com.example.dicodingevent.data.response.EventListResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int,
    ): Call<EventListResponse>

    @GET("events")
    fun searchEvents(
        @Query("q") query: String,
    ): Call<EventListResponse>

    @GET("events")
    fun getEventsLimit(
        @Query("limit") limit: Int,
    ): Call<EventListResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: Int,
    ): Call<EventDetailResponse>
}