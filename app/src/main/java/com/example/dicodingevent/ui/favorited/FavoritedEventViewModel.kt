package com.example.dicodingevent.ui.favorited

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository

class FavoritedEventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getFavoritedEvent() = eventRepository.getFavoritedEvent()
}