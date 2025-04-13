package com.example.dicodingevent.ui.favorited

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.di.Injection

class FavoritedEventViewModelFactory private constructor(private val eventRepository: EventRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritedEventViewModel::class.java)) {
            return FavoritedEventViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: FavoritedEventViewModelFactory? = null
        fun getInstance(context: Context): FavoritedEventViewModelFactory = instance ?: synchronized(this) {
            instance ?: FavoritedEventViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}