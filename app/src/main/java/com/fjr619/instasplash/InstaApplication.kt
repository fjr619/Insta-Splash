package com.fjr619.instasplash

import android.app.Application
import com.fjr619.instasplash.di.AppModule
import com.fjr619.instasplash.di.DataModule
import com.fjr619.instasplash.di.DomainModule
import com.fjr619.instasplash.di.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class InstaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@InstaApplication)
            modules(
//                databaseModule,
//                remoteModule,
//                repositoryModule,
//                viewModelModule,
//                networkObserveModule

                AppModule().module
            )
        }
    }
}