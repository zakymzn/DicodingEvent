package com.example.dicodingevent.data.local.room

import android.content.Context
import androidx.room.*
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.dicodingevent.data.local.entity.FavoriteEventEntity

@Database(entities = [EventEntity::class, FavoriteEventEntity::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var instance: EventDatabase? = null
        fun getInstance(context: Context): EventDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java, "Event.db"
                ).build()
            }
    }
}