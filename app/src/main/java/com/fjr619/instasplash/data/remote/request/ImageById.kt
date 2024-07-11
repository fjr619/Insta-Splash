package com.fjr619.instasplash.data.remote.request

import io.ktor.resources.Resource

@Resource("/photos")
class ImageById {
    @Resource("{imageId}")
    class Id(val parent: ImageById = ImageById(), val imageId: String)
}