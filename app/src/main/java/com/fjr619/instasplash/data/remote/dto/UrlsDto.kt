package com.fjr619.instasplash.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UrlsDto(
    val full: String,
    val raw: String,
    val regular: String,
    val small: String,
    val thumb: String
)