package com.example.dicodingevent.data.retrofit
import com.example.dicodingevent.data.response.EventDetailResponse
import com.example.dicodingevent.data.response.EventListResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events?active={active}")
    fun getEvents(
        @Path("active") active: Int,
    ): Call<EventListResponse>

    @GET("events?q={query}")
    fun searchEvents(
        @Path("query") query: String,
    ): Call<EventListResponse>

    @GET("events?limit={limit}")
    fun getEventsLimit(
        @Path("limit") limit: Int,
    ): Call<EventListResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: String
    ): Call<EventDetailResponse>
}