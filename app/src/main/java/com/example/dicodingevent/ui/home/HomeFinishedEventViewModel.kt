package com.example.dicodingevent.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.local.entity.EventEntity
import kotlinx.coroutines.launch

class HomeFinishedEventViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getHomeFinishedEvent() = eventRepository.getHomeFinishedEvent()

    fun getFavoritedEvent() = eventRepository.getFavoritedEvent()

    fun saveEvent(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.setEventFavorite(event, true)
        }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.setEventFavorite(event, false)
        }
    }
}