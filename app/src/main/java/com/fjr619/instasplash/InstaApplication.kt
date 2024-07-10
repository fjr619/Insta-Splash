package com.fjr619.instasplash

import android.app.Application
import com.fjr619.instasplash.di.remoteModule
import com.fjr619.instasplash.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class InstaApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@InstaApplication)
            modules(
                remoteModule,
                viewModelModule
            )
        }
    }
}