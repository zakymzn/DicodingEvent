package com.example.dicodingevent.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.local.entity.EventEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailEventViewModel @Inject constructor(private val eventRepository: EventRepository, savedStateHandle: SavedStateHandle) : ViewModel() {

    val args = DetailFragmentArgs.fromSavedStateHandle(savedStateHandle)
    val eventId: Int = args.id

    fun getDetailEvent() = eventRepository.getDetailEvent(eventId)

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