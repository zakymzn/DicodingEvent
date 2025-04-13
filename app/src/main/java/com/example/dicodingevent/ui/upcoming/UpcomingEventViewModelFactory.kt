package com.example.dicodingevent.ui.upcoming

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.di.Injection

class UpcomingEventViewModelFactory private constructor(private val eventRepository: EventRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpcomingEventViewModel::class.java)) {
            return UpcomingEventViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: UpcomingEventViewModelFactory? = null
        fun getInstance(context: Context): UpcomingEventViewModelFactory = instance ?: synchronized(this) {
            instance ?: UpcomingEventViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}