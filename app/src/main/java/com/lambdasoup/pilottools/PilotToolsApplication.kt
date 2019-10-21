package com.lambdasoup.pilottools

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    viewModel { ClientsViewModel() }
    viewModel { InstrumentsViewModel() }
}

// TODO try to get rid of @suppress, Android Studio bug?
@Suppress("unused")
class PilotToolsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@PilotToolsApplication)
            modules(appModule)
        }
    }
}
