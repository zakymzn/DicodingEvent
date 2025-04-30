package com.example.dicodingevent.ui.finished

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository

class FinishedEventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getFinishedEvent() = eventRepository.getFinishedEvent()
}