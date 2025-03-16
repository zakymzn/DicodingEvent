package com.example.dicodingevent.ui.search

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

class SearchEventViewModel : ViewModel() {

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "SearchEventViewModel"
        private const val ALL_EVENT = -1
    }

    fun searchEvent(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchEvents(ALL_EVENT, query)
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