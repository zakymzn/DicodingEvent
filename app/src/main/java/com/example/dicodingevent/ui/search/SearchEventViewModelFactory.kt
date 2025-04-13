package com.example.dicodingevent.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.di.Injection

class SearchEventViewModelFactory private constructor(private val eventRepository: EventRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchEventViewModel::class.java)) {
            return SearchEventViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: SearchEventViewModelFactory? = null
        fun getInstance(context: Context): SearchEventViewModelFactory = instance ?: synchronized(this) {
            instance ?: SearchEventViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}