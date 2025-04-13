package com.example.dicodingevent.ui.finished

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.di.Injection

class FinishedEventViewModelFactory private constructor(private val eventRepository: EventRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinishedEventViewModel::class.java)) {
            return FinishedEventViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: FinishedEventViewModelFactory? = null
        fun getInstance(context: Context): FinishedEventViewModelFactory = instance ?: synchronized(this) {
            instance ?: FinishedEventViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}