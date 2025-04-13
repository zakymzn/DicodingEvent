package com.example.dicodingevent.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.dicodingevent.BuildConfig
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.dicodingevent.data.local.room.EventDao
import com.example.dicodingevent.data.remote.response.EventListResponse
import com.example.dicodingevent.data.remote.retrofit.ApiService
import com.example.dicodingevent.utils.AppExecutors
import retrofit2.*

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {
    fun getHomeUpcomingEvent(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(UPCOMING_EVENT, 5)
            val events = response.listEvents
            val eventList = events.map { event ->
                val isFavorited = eventDao.isEventFavorited(event.id)
                EventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.registrants,
                    event.imageLogo,
                    event.ownerName,
                    event.quota,
                    event.beginTime,
                    event.endTime,
                    event.category,
                    isFavorited
                )
            }
            eventDao.deleteAll()
            eventDao.insertEvent(eventList)
        } catch (e: Exception) {
            Log.d("EventRepository", "getUpcomingEvent: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventEntity>>> = eventDao.getEvents().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getHomeFinishedEvent(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(FINISHED_EVENT, 5)
            val events = response.listEvents
            val eventList = events.map { event ->
                val isFavorited = eventDao.isEventFavorited(event.id)
                EventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.registrants,
                    event.imageLogo,
                    event.ownerName,
                    event.quota,
                    event.beginTime,
                    event.endTime,
                    event.category,
                    isFavorited
                )
            }
            eventDao.deleteAll()
            eventDao.insertEvent(eventList)
        } catch (e: Exception) {
            Log.d("EventRepository", "getFinishedEvent: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventEntity>>> = eventDao.getEvents().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getUpcomingEvent(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(UPCOMING_EVENT, 40)
            val events = response.listEvents
            val eventList = events.map { event ->
                val isFavorited = eventDao.isEventFavorited(event.id)
                EventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.registrants,
                    event.imageLogo,
                    event.ownerName,
                    event.quota,
                    event.beginTime,
                    event.endTime,
                    event.category,
                    isFavorited
                )
            }
            eventDao.deleteAll()
            eventDao.insertEvent(eventList)
        } catch (e: Exception) {
            Log.d("EventRepository", "getUpcomingEvent: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventEntity>>> = eventDao.getEvents().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getFinishedEvent(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(FINISHED_EVENT, 40)
            val events = response.listEvents
            val eventList = events.map { event ->
                val isFavorited = eventDao.isEventFavorited(event.id)
                EventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.registrants,
                    event.imageLogo,
                    event.ownerName,
                    event.quota,
                    event.beginTime,
                    event.endTime,
                    event.category,
                    isFavorited
                )
            }
            eventDao.deleteAll()
            eventDao.insertEvent(eventList)
        } catch (e: Exception) {
            Log.d("EventRepository", "getFinishedEvent: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventEntity>>> = eventDao.getEvents().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getAllEvent(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(ALL_EVENT, 40)
            val events = response.listEvents
            val eventList = events.map { event ->
                val isFavorited = eventDao.isEventFavorited(event.id)
                EventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.registrants,
                    event.imageLogo,
                    event.ownerName,
                    event.quota,
                    event.beginTime,
                    event.endTime,
                    event.category,
                    isFavorited
                )
            }
            eventDao.deleteAll()
            eventDao.insertEvent(eventList)
        } catch (e: Exception) {
            Log.d("EventRepository", "getAllEvent: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventEntity>>> = eventDao.getEvents().map { Result.Success(it) }
        emitSource(localData)
    }

    fun searchEvent(query: String): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.searchEvents(ALL_EVENT, query)
            val events = response.listEvents
            val eventList= events.map { event ->
                val isFavorited = eventDao.isEventFavorited(event.id)
                EventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.registrants,
                    event.imageLogo,
                    event.ownerName,
                    event.quota,
                    event.beginTime,
                    event.endTime,
                    event.category,
                    isFavorited
                )
            }
            eventDao.deleteAll()
            eventDao.insertEvent(eventList)
        } catch (e: Exception) {
            Log.d("EventRepository", "searchEvent: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventEntity>>> = eventDao.getEvents().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getFavoritedEvent(): LiveData<List<EventEntity>> {
        return eventDao.getFavoritedEvent()
    }

    suspend fun setEventFavorite(event: EventEntity, favoriteState: Boolean) {
        event.isFavorited = favoriteState
        eventDao.updateEvent(event)
    }

    companion object {
        private const val UPCOMING_EVENT = 1
        private const val FINISHED_EVENT = 0
        private const val ALL_EVENT = -1

        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}