package com.example.dicodingevent.data.local.room

import androidx.room.*
import androidx.lifecycle.LiveData
import com.example.dicodingevent.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM event ORDER BY id DESC")
    fun getEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE id = :id")
    fun getDetailEvent(id: Int?): LiveData<EventEntity>

    @Query("SELECT * FROM event WHERE favorited = 1")
    fun getFavoritedEvent(): LiveData<List<EventEntity>>

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
}