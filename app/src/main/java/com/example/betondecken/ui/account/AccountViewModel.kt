package com.example.betondecken.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccountViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este es el fragmento para la cuenta"
    }
    val text: LiveData<String> = _text
}