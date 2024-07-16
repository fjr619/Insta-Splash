package com.fjr619.instasplash.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fjr619.instasplash.data.local.entities.FavoriteImageEntity
import com.fjr619.instasplash.data.local.entities.UnsplashImageDao
import com.fjr619.instasplash.data.local.entities.UnsplashImageEntity
import com.fjr619.instasplash.data.local.entities.UnsplashRemoteKeys

@Database(
    entities = [FavoriteImageEntity::class, UnsplashImageEntity::class, UnsplashRemoteKeys::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(
            from = 1, to = 2
        )
    ]
)
abstract class ImageSplashDatabase: RoomDatabase() {
    abstract fun favoriteImagesDao(): FavoriteImagesDao
    abstract fun unsplashImageDao(): UnsplashImageDao
}