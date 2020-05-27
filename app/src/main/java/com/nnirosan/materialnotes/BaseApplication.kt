package com.nnirosan.materialnotes

import android.app.Application
import com.nnirosan.materialnotes.di.appModule
import com.nnirosan.materialnotes.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(appModule, viewModelModule)
        }
    }
}