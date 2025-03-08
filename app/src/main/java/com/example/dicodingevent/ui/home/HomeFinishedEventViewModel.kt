package com.example.dicodingevent.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.response.EventListResponse
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFinishedEventViewModel : ViewModel() {

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> = _listEvents

//    private val _listEventsItem = MutableLiveData<ListEventsItem>()
//    val listEventsItem: LiveData<ListEventsItem> = _listEventsItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "HomeViewModel"
        private const val UPCOMING_EVENT = 1
        private const val FINISHED_EVENT = 0
    }

    init {
        getUpcomingEvents()
        getFinishedEvents()
    }

    private fun getUpcomingEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(UPCOMING_EVENT)
        client.enqueue(object : Callback<EventListResponse> {
            override fun onResponse(
                call: Call<EventListResponse>,
                response: Response<EventListResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listEvents.value = response.body()?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun getFinishedEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(FINISHED_EVENT)
        client.enqueue(object : Callback<EventListResponse> {
            override fun onResponse(
                call: Call<EventListResponse>,
                response: Response<EventListResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listEvents.value = response.body()?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}