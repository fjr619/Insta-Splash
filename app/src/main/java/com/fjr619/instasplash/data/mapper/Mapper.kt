package com.fjr619.instasplash.data.mapper

import com.fjr619.instasplash.data.remote.dto.UnsplashImageDto
import com.fjr619.instasplash.domain.model.UnsplashImage

fun UnsplashImageDto.toDomainModel(): UnsplashImage {
    return UnsplashImage(
        id = this.id,
        imageUrlSmall = this.urls.small,
        imageUrlRegular = this.urls.regular,
        imageUrlRaw = this.urls.raw,
        photographerName = this.user.name,
        photographerUsername = this.user.username,
        photographerProfileImgUrl = this.user.profileImage.small,
        photographerProfileLink = this.user.links.html,
        width = this.width,
        height = this.height,
        description = description
    )
}

fun List<UnsplashImageDto>.toDomainModelList(): List<UnsplashImage> {
    return this.map { it.toDomainModel() }
}