package com.example.dicodingevent.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.di.Injection

class HomeUpcomingEventViewModelFactory private constructor(private val eventRepository: EventRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeUpcomingEventViewModel::class.java)) {
            return HomeUpcomingEventViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: HomeUpcomingEventViewModelFactory? = null
        fun getInstance(context: Context): HomeUpcomingEventViewModelFactory = instance ?: synchronized(this) {
            instance ?: HomeUpcomingEventViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}