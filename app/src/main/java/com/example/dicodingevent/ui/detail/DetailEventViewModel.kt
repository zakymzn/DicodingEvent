package com.example.dicodingevent.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.response.Event
import com.example.dicodingevent.data.response.EventDetailResponse
import com.example.dicodingevent.data.retrofit.ApiConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailEventViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailEvent = MutableLiveData<Event?>()
    val detailEvent: LiveData<Event?> = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val args = DetailFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val eventId = args.id

    init {
        getDetailEvent(eventId)
    }

    private fun getDetailEvent(id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventDetail(id)
        client.enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailEvent.value = response.body()?.event
                } else {
                    _errorMessage.value = response.message()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message.toString()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailEventViewModel"
    }
}