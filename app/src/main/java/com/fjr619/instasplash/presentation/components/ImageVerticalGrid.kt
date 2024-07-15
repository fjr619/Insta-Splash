package com.fjr619.instasplash.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.fjr619.instasplash.domain.model.UnsplashImage

@Composable
fun ImagesVerticalGrid(
    modifier: Modifier = Modifier,
    lazyStaggeredGridState: LazyStaggeredGridState,
    images: LazyPagingItems<UnsplashImage>,
    onImageClick: (String) -> Unit,
    onImageDragStart: (UnsplashImage?) -> Unit,
    onImageDragEnd: () -> Unit,
) {
    Column {
        Spacer(modifier = modifier)
        LazyVerticalStaggeredGrid(
            state = lazyStaggeredGridState,
            columns = StaggeredGridCells.Adaptive(120.dp),
            contentPadding = PaddingValues(10.dp),
            verticalItemSpacing = 10.dp,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                count = images.itemCount,
                key = images.itemKey()
            ) { index ->
                val image = images[index]
                ImageCard(
                    image = image,
                    modifier = Modifier
                        .clickable { image?.id?.let { onImageClick(it) } }
                        .pointerInput(Unit) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = { onImageDragStart(image) },
                                onDragCancel = { onImageDragEnd() },
                                onDragEnd = { onImageDragEnd() },
                                onDrag = { _, _ -> }
                            )
                        }
                )
            }
        }
    }

}