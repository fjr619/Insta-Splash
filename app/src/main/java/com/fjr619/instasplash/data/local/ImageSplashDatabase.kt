package com.fjr619.instasplash.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fjr619.instasplash.data.local.entities.FavoriteImageEntity

@Database(
    entities = [FavoriteImageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ImageSplashDatabase: RoomDatabase() {
    abstract fun favoriteImagesDao(): FavoriteImagesDao
}