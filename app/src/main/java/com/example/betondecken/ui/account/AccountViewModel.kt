package com.example.betondecken.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccountViewModel : ViewModel() {

    private val _userName = MutableLiveData("Luis De Los Palotes")
    val userName: LiveData<String> get() = _userName

    private val _email = MutableLiveData("ldelospalotes@micorreo.com")
    val email: LiveData<String> get() = _email

    private val _phone = MutableLiveData("+51 456-6759")
    val phone: LiveData<String> get() = _phone

    private val _totalOrder = MutableLiveData("1000 Kg")
    val totalOrder: LiveData<String> get() = _totalOrder

    private val _pendingDelivery = MutableLiveData("500 Kg")
    val pendingDelivery: LiveData<String> get() = _pendingDelivery
}
