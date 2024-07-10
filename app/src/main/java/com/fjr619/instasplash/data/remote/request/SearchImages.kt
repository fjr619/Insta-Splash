package com.fjr619.instasplash.data.remote.request

import io.ktor.resources.Resource
import kotlinx.serialization.SerialName

@Resource("/search/photos")
class SearchImages(
    @SerialName("page") val page: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("query") val query: String
)