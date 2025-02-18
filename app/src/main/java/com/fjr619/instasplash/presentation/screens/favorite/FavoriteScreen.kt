package com.fjr619.instasplash.presentation.screens.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.fjr619.instasplash.domain.model.UnsplashImage
import com.fjr619.instasplash.presentation.components.ImageSplashAppBar
import com.fjr619.instasplash.presentation.components.ImagesVerticalGrid
import com.fjr619.instasplash.presentation.components.PreviewImageCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    favoriteImages: LazyPagingItems<UnsplashImage>,
    favoriteImageIds: List<String>,
    scrollBehavior: TopAppBarScrollBehavior,
    onImageClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onBackClick: () -> Unit,
    onACtion: (FavoritesAction) -> Unit
) {

    var showImagePreview by remember { mutableStateOf(false) }
    var activeImage by remember { mutableStateOf<UnsplashImage?>(null) }
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()

    Scaffold(
        topBar = {
            ImageSplashAppBar(
                title = "Favorite Images",
                scrollBehavior = scrollBehavior,
                onSearchClick = onSearchClick,
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .background(MaterialTheme.colorScheme.surface),
        ) {
            ImagesVerticalGrid(
                lazyStaggeredGridState = lazyStaggeredGridState,
                images = favoriteImages,
                loadState = favoriteImages.loadState,
                favoriteImageIds = favoriteImageIds,
                onImageClick = onImageClick,
                onImageDragStart = { image ->
                    activeImage = image
                    showImagePreview = true
                },
                onImageDragEnd = {
//                    activeImage = null
                    showImagePreview = false
                },
                onToggleFavoriteStatus = {
                    onACtion(FavoritesAction.ToggleFavoriteStatus(it))
                }
            )

            PreviewImageCard(
                modifier = Modifier.padding(20.dp),
                isVisible = showImagePreview,
                image = activeImage
            )
        }
    }
}