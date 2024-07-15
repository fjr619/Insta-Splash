package com.fjr619.instasplash.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fjr619.instasplash.data.util.Constants.REMOTE_KEYS_TABLE

@Entity(tableName = REMOTE_KEYS_TABLE)
data class UnsplashRemoteKeys(
    @PrimaryKey
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)