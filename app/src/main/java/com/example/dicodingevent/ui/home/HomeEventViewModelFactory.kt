package com.example.dicodingevent.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.di.Injection

class HomeEventViewModelFactory private constructor(private val eventRepository: EventRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeEventViewModel::class.java)) {
            return HomeEventViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: HomeEventViewModelFactory? = null
        fun getInstance(context: Context): HomeEventViewModelFactory = instance ?: synchronized(this) {
            instance ?: HomeEventViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}