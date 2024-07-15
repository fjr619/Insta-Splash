package com.fjr619.instasplash.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fjr619.instasplash.data.util.Constants.UNSPLASH_IMAGE_TABLE

@Entity(tableName = UNSPLASH_IMAGE_TABLE)
data class UnsplashImageEntity(
    @PrimaryKey
    val id: String,
    val imageUrlSmall: String,
    val imageUrlRegular: String,
    val imageUrlRaw: String,
    val photographerName: String,
    val photographerUsername: String,
    val photographerProfileImgUrl: String,
    val photographerProfileLink: String,
    val width: Int,
    val height: Int,
    val description: String?
)