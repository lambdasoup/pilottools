package com.lambdasoup.pilottools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InstrumentsViewModel : ViewModel() {
    private val _heading = MutableLiveData<Float>().apply {
        value = 230f
    }
    val heading: LiveData<Float> = _heading

    fun turnRight() {
        _heading.value = (_heading.value!! + 1) % 360.0f
    }

    fun turnLeft() {
        _heading.value = (_heading.value!! - 1) % 360.0f
    }
}
