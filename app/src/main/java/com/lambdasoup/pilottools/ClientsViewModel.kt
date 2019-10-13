package com.lambdasoup.pilottools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClientsViewModel : ViewModel() {
    private val _example = MutableLiveData<String>().apply {
        value = "Hello Databinding!"
    }
    val example: LiveData<String> = _example
}
