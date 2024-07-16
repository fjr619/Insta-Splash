package com.fjr619.instasplash.presentation.screens.full_image

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.animateZoomBy
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.fjr619.instasplash.presentation.components.ImageSplashLoading
import com.fjr619.instasplash.presentation.screens.full_image.components.DownloadOptionsBottomSheet
import com.fjr619.instasplash.presentation.screens.full_image.components.FullImageTopAppBar
import com.fjr619.instasplash.presentation.screens.full_image.components.ImageDownloadOption
import kotlinx.coroutines.launch
import kotlin.math.max

data class FullImageScreenNavArgs(
    val imageId: String,
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FullImageScreen(
    state: FullImageState,
    showBars: Boolean,
    onBackClick: () -> Unit,
    onPhotographerNameClick: (String) -> Unit,
    onImageDownloadClick: (String, String?) -> Unit,
    updateShowbars: (Boolean) -> Unit,
    toggleStatusBars: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isDownloadBottomSheetOpen by remember { mutableStateOf(false) }

    DownloadOptionsBottomSheet(
        isOpen = isDownloadBottomSheetOpen,
        sheetState = sheetState,
        onDismissRequest = { isDownloadBottomSheetOpen = false },
        onOptionClick = { option ->
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) isDownloadBottomSheetOpen = false
            }
            val url = when (option) {
                ImageDownloadOption.SMALL -> state.image?.imageUrlSmall
                ImageDownloadOption.MEDIUM -> state.image?.imageUrlRegular
                ImageDownloadOption.ORIGINAL -> state.image?.imageUrlRaw
            }
            url?.let {
                onImageDownloadClick(it, state.image?.description?.take(20))
            }
        }
    )

    Scaffold(
        topBar = {
            FullImageTopAppBar(
                image = state.image,
                isError = state.isError,
                isVisible = showBars,
                onBackClick = onBackClick,
                onPhotographerNameClick = onPhotographerNameClick,
                onDownloadImgClick = {
                    isDownloadBottomSheetOpen = true
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                //state data API
                if (state.isLoading) {
                    ImageSplashLoading()
                } else if (state.isError) {
                    Text(text = "Error")
                } else {
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val imageLoader = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(context)
                                .data(state.image?.imageUrlRaw)
                                .crossfade(true)
                                .build(),
                        )

                        var scale by remember { mutableFloatStateOf(1f) }
                        var offset by remember { mutableStateOf(Offset.Zero) }
                        val isImageZoomed: Boolean by remember { derivedStateOf { scale != 1f } }
                        val transformState =
                            rememberTransformableState { zoomChange, offsetChange, _ ->
                                scale = max(scale * zoomChange, 1f)
                                val maxX = (constraints.maxWidth * (scale - 1)) / 2
                                val maxY = (constraints.maxHeight * (scale - 1)) / 2
                                offset = Offset(
                                    x = (offset.x + offsetChange.x).coerceIn(-maxX, maxX),
                                    y = (offset.y + offsetChange.y).coerceIn(-maxY, maxY)
                                )
                            }

                        when (imageLoader.state) {
                            is AsyncImagePainter.State.Loading -> {
                                ImageSplashLoading()
                            }

                            is AsyncImagePainter.State.Error -> {
                                Text(text = "Error")
                            }

                            else -> Unit
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .combinedClickable(
                                    onDoubleClick = {
                                        if (isImageZoomed) {
                                            scale = 1f
                                            offset = Offset.Zero
                                        } else {
                                            scope.launch { transformState.animateZoomBy(zoomFactor = 3f) }
                                        }
                                    },
                                    onClick = {
                                        updateShowbars(!showBars)
                                        toggleStatusBars()
                                    },
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = imageLoader,
                                contentDescription = null,
                                modifier = Modifier
                                    .transformable(transformState)

                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                        translationX = offset.x
                                        translationY = offset.y
                                    }
                            )
                        }
                    }
                }

            }
        }
    }
}