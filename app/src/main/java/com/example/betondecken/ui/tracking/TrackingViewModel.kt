package com.example.betondecken.ui.tracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TrackingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este es el fragmento para el Tracking"
    }
    val text: LiveData<String> = _text
}