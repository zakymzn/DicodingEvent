package com.example.dicodingevent.data.local.room

import androidx.room.*
import androidx.lifecycle.LiveData
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.dicodingevent.data.local.entity.FavoriteEventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM event ORDER BY id DESC")
    fun getAllEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE DATETIME('now') < endTime ORDER BY id DESC LIMIT 5")
    fun getFiveUpcomingEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE DATETIME('now') < endTime ORDER BY id DESC")
    fun getUpcomingEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE DATETIME('now') >= endTime ORDER BY id DESC LIMIT 5")
    fun getFiveFinishedEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE DATETIME('now') >= endTime ORDER BY id DESC")
    fun getFinishedEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE id = :id")
    fun getDetailEvent(id: Int?): LiveData<EventEntity>

    @Query("SELECT * FROM favorite_event")
    fun getFavoritedEvent(): LiveData<List<FavoriteEventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDetailEvent(event: EventEntity)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("DELETE FROM event WHERE favorited = 0")
    suspend fun deleteAll()

    @Query("DELETE FROM event WHERE id = :id")
    suspend fun deleteById(id: Int?)

    @Query("SELECT EXISTS(SELECT * FROM event WHERE id = :id AND favorited = 1)")
    suspend fun isEventFavorited(id: Int?): Boolean

    @Query("SELECT * FROM favorite_event WHERE id = :id")
    fun getFavoriteEventById(id: Int?): LiveData<FavoriteEventEntity>

    @Insert
    suspend fun insertFavoriteEvent(favoriteEvent: FavoriteEventEntity)

    @Delete
    suspend fun deleteFavoriteEvent(favoriteEvent: FavoriteEventEntity)
}