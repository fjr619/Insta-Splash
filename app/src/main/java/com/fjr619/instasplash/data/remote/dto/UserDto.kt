package com.fjr619.instasplash.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val links: UserLinksDto,
    val name: String,
    @SerialName("profile_image")
    val profileImage: ProfileImageDto,
    val username: String
)



