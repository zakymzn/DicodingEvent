package com.example.dicodingevent.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.di.Injection

class HomeFinishedEventViewModelFactory private constructor(private val eventRepository: EventRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeFinishedEventViewModel::class.java)) {
            return HomeFinishedEventViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: HomeFinishedEventViewModelFactory? = null
        fun getInstance(context: Context): HomeFinishedEventViewModelFactory = instance ?: synchronized(this) {
            instance ?: HomeFinishedEventViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}