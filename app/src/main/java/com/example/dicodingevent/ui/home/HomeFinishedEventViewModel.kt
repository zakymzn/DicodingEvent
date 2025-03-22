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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        getFinishedEvents()
    }

    private fun getFinishedEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(FINISHED_EVENT, 5)
        client.enqueue(object : Callback<EventListResponse> {
            override fun onResponse(
                call: Call<EventListResponse>,
                response: Response<EventListResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listEvents.value = response.body()?.listEvents
                } else {
                    _errorMessage.value = response.message()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message.toString()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "FinishedEventViewModel"
        private const val FINISHED_EVENT = 0
    }
}