package com.lambdasoup.pilottools

import androidx.lifecycle.LiveData
import com.lambdasoup.xpnet.Client
import com.lambdasoup.xpnet.Registry

class XpConnection(
    private val registry: Registry
) {

    val clients = object: LiveData<Client>() {
        override fun onActive() {
            registry.listen { client -> }
        }
    }

}
