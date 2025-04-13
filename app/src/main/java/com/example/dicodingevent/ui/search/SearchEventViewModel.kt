package com.example.dicodingevent.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.dicodingevent.data.remote.response.EventListResponse
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchEventViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun searchEvent(query: String) = eventRepository.searchEvent(query)

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

//    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
//    val listEvents: LiveData<List<ListEventsItem>> = _listEvents
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _errorMessage = MutableLiveData<String>()
//    val errorMessage: LiveData<String> = _errorMessage
//
//    fun searchEvent(query: String) {
//        _isLoading.value = true
//        val client = ApiConfig.getApiService().searchEvents(ALL_EVENT, query)
//        client.enqueue(object : Callback<EventListResponse> {
//            override fun onResponse(
//                call: Call<EventListResponse>,
//                response: Response<EventListResponse>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    _listEvents.value = response.body()?.listEvents
//                } else {
//                    _errorMessage.value = response.message()
//                    Log.e(TAG, "onFailure: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
//                _isLoading.value = false
//                _errorMessage.value = t.message.toString()
//                Log.e(TAG, "onFailure: ${t.message.toString()}")
//            }
//        })
//    }
//
//    companion object {
//        private const val TAG = "SearchEventViewModel"
//        private const val ALL_EVENT = -1
//    }
}