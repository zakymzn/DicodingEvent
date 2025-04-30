package com.example.dicodingevent.ui.search

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository

class SearchEventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun searchEvent(query: String) = eventRepository.searchEvent(query)
}