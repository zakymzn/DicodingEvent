package com.example.dicodingevent.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FinishedViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Finished Fragment"
    }
    val text: LiveData<String> = _text
}