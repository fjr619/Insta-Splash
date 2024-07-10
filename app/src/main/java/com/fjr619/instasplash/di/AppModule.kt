package com.fjr619.instasplash.di

import com.fjr619.instasplash.data.remote.RemoteDatasource
import com.fjr619.instasplash.data.remote.RemoteDatasourceImpl
import com.fjr619.instasplash.data.remote.createHttpClient
import com.fjr619.instasplash.presentation.home.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val remoteModule = module {
    single<HttpClientEngine> { Android.create() }
    single<HttpClient> { createHttpClient(get()) }
    single<RemoteDatasource> { RemoteDatasourceImpl(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
}