package com.lambdasoup.pilottools.clients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lambdasoup.pilottools.XpConnection
import com.lambdasoup.xpnet.Client

class ClientsViewModel(
    private val xpConnection: XpConnection
) : ViewModel() {
    private val _clients = xpConnection.clients
    val clients: LiveData<Client> = _clients
}
