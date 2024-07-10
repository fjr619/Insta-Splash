package com.fjr619.instasplash.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UnsplashImageDto(
    val id: String,
    val description: String?,
    val height: Int,
    val width: Int,
    val urls: UrlsDto,
    val user: UserDto,
)

