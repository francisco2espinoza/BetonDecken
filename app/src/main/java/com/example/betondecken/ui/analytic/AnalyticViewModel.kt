package com.example.betondecken.ui.analytic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnalyticViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "asdasdas"
    }
    val text: LiveData<String> = _text
}