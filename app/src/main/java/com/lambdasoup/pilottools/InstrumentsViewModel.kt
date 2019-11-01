package com.lambdasoup.pilottools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lambdasoup.pilottools.ehsi.Ehsi

class InstrumentsViewModel : ViewModel() {
    private val _ehsi = MutableLiveData<Ehsi>().apply {
        value = Ehsi(heading = 230f)
    }
    val ehsi: LiveData<Ehsi> = _ehsi

    fun turnRight() {
        _ehsi.value = Ehsi(heading = 215.2f)
    }

    fun turnLeft() {
        _ehsi.value = Ehsi(heading = 77.8f)
    }
}
