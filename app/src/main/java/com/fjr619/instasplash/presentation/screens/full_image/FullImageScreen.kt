package com.fjr619.instasplash.presentation.screens.full_image

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.fjr619.instasplash.presentation.components.ImageSplashLoading
import com.fjr619.instasplash.presentation.screens.destinations.ProfileScreenDestination
import com.fjr619.instasplash.presentation.screens.full_image.components.FullImageTopAppBar
import com.fjr619.instasplash.presentation.util.rememberWindowInsetsController
import com.fjr619.instasplash.presentation.util.toggleStatusBars
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.max

data class FullImageScreenNavArgs(
    val imageId: String,
)

@RootNavGraph
@Destination(
    navArgsDelegate = FullImageScreenNavArgs::class
)
@Composable
fun FullImageScreen(
    navigator: DestinationsNavigator,
    viewModel: FullImageViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    FullImageContent(
        fullImageState = state,
        onBackClick = { navigator.navigateUp() },
        onPhotographerNameClick = {
            navigator.navigate(ProfileScreenDestination(profileLink = it))
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullImageContent(
    fullImageState: FullImageState,
    onBackClick: () -> Unit,
    onPhotographerNameClick: (String) -> Unit
) {

    val scope = rememberCoroutineScope()
    var showBars by rememberSaveable { mutableStateOf(false) }
    val windowInsetsController = rememberWindowInsetsController()


    LaunchedEffect(key1 = showBars) {
        windowInsetsController.toggleStatusBars(show = showBars)
    }

    BackHandler(enabled = !showBars) {
        windowInsetsController.toggleStatusBars(show = true)
        onBackClick()
    }

    Scaffold(
        topBar = {
            FullImageTopAppBar(
                image = fullImageState.image,
                isError = fullImageState.isError,
                isVisible = showBars,
                onBackClick = onBackClick,
                onPhotographerNameClick = onPhotographerNameClick,
                onDownloadImgClick = {}
            )
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                println("${fullImageState.isLoading} ${fullImageState.isError}")

                //state data API
                if (fullImageState.isLoading) {
                    ImageSplashLoading()
                } else if (fullImageState.isError) {
                    println("aa ini error ")
                    Text(text = "Error")
                } else {
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val imageLoader = rememberAsyncImagePainter(
                            model = fullImageState.image?.imageUrlRaw,
                            onState = { imageState ->
                                println("state $imageState")
                            }
                        )

                        var scale by remember { mutableFloatStateOf(1f) }
                        var offset by remember { mutableStateOf(Offset.Zero) }
                        val isImageZoomed: Boolean by remember { derivedStateOf { scale != 1f } }
                        val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
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
                                        showBars = !showBars
                                        windowInsetsController.toggleStatusBars(show = showBars)
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