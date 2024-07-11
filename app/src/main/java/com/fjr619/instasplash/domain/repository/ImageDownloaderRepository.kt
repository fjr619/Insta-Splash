package com.fjr619.instasplash.domain.repository

interface ImageDownloaderRepository {
    fun downloadFile(url: String, fileName: String?)
}