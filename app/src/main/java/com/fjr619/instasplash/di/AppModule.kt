package com.fjr619.instasplash.di

import androidx.room.Room
import com.fjr619.instasplash.data.local.FavoriteImagesDao
import com.fjr619.instasplash.data.local.ImageSplashDatabase
import com.fjr619.instasplash.data.remote.RemoteDatasource
import com.fjr619.instasplash.data.remote.RemoteDatasourceImpl
import com.fjr619.instasplash.data.remote.createHttpClient
import com.fjr619.instasplash.data.repository.ImageDownloaderRepositoryImpl
import com.fjr619.instasplash.data.repository.ImageRepositoryImpl
import com.fjr619.instasplash.data.repository.NetworkConnectivityObserveImpl
import com.fjr619.instasplash.data.util.Constants
import com.fjr619.instasplash.domain.repository.ImageDownloaderRepository
import com.fjr619.instasplash.domain.repository.ImageRepository
import com.fjr619.instasplash.domain.repository.NetworkConnectivityObserver
import com.fjr619.instasplash.presentation.screens.favorite.FavoritesViewModel
import com.fjr619.instasplash.presentation.screens.full_image.FullImageViewModel
import com.fjr619.instasplash.presentation.screens.home.HomeViewModel
import com.fjr619.instasplash.presentation.screens.search.SearchViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single<ImageSplashDatabase> { Room.databaseBuilder(
        androidContext(),
        ImageSplashDatabase::class.java,
        Constants.IMAGE_SPLASH_DB
    ).build() }
    single<FavoriteImagesDao> { (get() as ImageSplashDatabase).favoriteImagesDao() }
}

val remoteModule = module {
    single<HttpClientEngine> { Android.create() }
    single<HttpClient> { createHttpClient(get()) }
    single<RemoteDatasource> { RemoteDatasourceImpl(get()) }
}

val repositoryModule = module {
    factory<ImageRepository> { ImageRepositoryImpl(get(), get()) }
    factory<ImageDownloaderRepository> { ImageDownloaderRepositoryImpl(androidContext())}
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { FullImageViewModel(get(), get(), get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { FavoritesViewModel(get()) }
}

val networkObserveModule = module {
    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    single<NetworkConnectivityObserver> { NetworkConnectivityObserveImpl(androidContext(), get()) }
}