package com.fjr619.instasplash.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class FailedResponse(
    val error: String
)