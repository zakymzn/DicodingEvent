package com.example.dicodingevent.ui.home

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository

class HomeEventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getHomeUpcomingEvent() = eventRepository.getHomeUpcomingEvent()
    fun getHomeFinishedEvent() = eventRepository.getHomeFinishedEvent()
}