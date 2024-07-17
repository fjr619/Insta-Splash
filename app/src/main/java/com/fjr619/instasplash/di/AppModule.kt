package com.fjr619.instasplash.di

import android.content.Context
import androidx.room.Room
import com.fjr619.instasplash.data.local.ImageSplashDatabase
import com.fjr619.instasplash.data.remote.createHttpClient
import com.fjr619.instasplash.data.util.Constants
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

//val databaseModule = module {
//    single<ImageSplashDatabase> { Room.databaseBuilder(
//        androidContext(),
//        ImageSplashDatabase::class.java,
//        Constants.IMAGE_SPLASH_DB
//    ).build() }
//    single<FavoriteImagesDao> { (get() as ImageSplashDatabase).favoriteImagesDao() }
//    single<UnsplashImageDao> { (get() as ImageSplashDatabase).unsplashImageDao() }
//}
//
//val remoteModule = module {
//    single<HttpClientEngine> { Android.create() }
//    single<HttpClient> { createHttpClient(get()) }
//    single<RemoteDatasource> { RemoteDatasourceImpl(get()) }
//}
//
//val repositoryModule = module {
//    factory<ImageRepository> { ImageRepositoryImpl(get(), get(), get(), get()) }
//    factory<ImageDownloaderRepository> { ImageDownloaderRepositoryImpl(androidContext())}
//}
//
//val viewModelModule = module {
//    viewModel { MainViewModel(get()) }
//    viewModel { HomeViewModel(get()) }
//    viewModel { FullImageViewModel(get(), get(), get()) }
//    viewModel { SearchViewModel(get()) }
//    viewModel { FavoritesViewModel(get()) }
//}
//
//val networkObserveModule = module {
//    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
//    single<NetworkConnectivityObserver> { NetworkConnectivityObserveImpl(androidContext()) }
//}

@Module
@ComponentScan(value = "com.fjr619.instasplash.data")
class DataModule {

    @Single
    fun getInstaSplashDatabase(context: Context) = Room.databaseBuilder(
        context,
        ImageSplashDatabase::class.java,
        Constants.IMAGE_SPLASH_DB
    ).build()

    @Single
    fun getFavoriteImageDao(database: ImageSplashDatabase) = database.favoriteImagesDao()

    @Single
    fun getUnsplashImageDao(database: ImageSplashDatabase) = database.unsplashImageDao()

    @Single
    fun getHttpClientEngine() = Android.create()

    @Single
    fun getHttpClient(httpClientEngine: HttpClientEngine) = createHttpClient(httpClientEngine)
}

@Module
@ComponentScan(value = "com.fjr619.instasplash.domain")
class DomainModule

@Module
@ComponentScan(value = "com.fjr619.instasplash.presentation")
class PresentationModule

@Module(includes = [DataModule::class, DomainModule::class, PresentationModule::class])
class AppModule