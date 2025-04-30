package com.example.dicodingevent.ui.upcoming

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository

class UpcomingEventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getUpcomingEvent() = eventRepository.getUpcomingEvent()
}