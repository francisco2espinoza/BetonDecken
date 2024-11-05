package com.example.betondecken.ui.analytic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnalyticViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este es el fragmento para la Analítica"
    }
    val text: LiveData<String> = _text
}