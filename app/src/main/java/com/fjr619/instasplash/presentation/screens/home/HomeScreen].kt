package com.fjr619.instasplash.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.fjr619.instasplash.R
import com.fjr619.instasplash.domain.model.UnsplashImage
import com.fjr619.instasplash.presentation.components.ImageSplashAppBar
import com.fjr619.instasplash.presentation.components.ImagesVerticalGrid
import com.fjr619.instasplash.presentation.components.PreviewImageCard
import com.fjr619.instasplash.presentation.screens.destinations.FavoritesScreenDestination
import com.fjr619.instasplash.presentation.screens.destinations.FullImageScreenDestination
import com.fjr619.instasplash.presentation.screens.destinations.SearchScreenDestination
import com.fjr619.instasplash.presentation.screens.favorite.FavoritesAction
import com.fjr619.instasplash.presentation.theme.InstaSplashTheme
import com.fjr619.instasplash.presentation.util.snackbar.AppSnackbarVisual
import com.fjr619.instasplash.presentation.util.snackbar.LocalSnackbarController
import com.fjr619.instasplash.presentation.util.snackbar.SnackbarController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(true)
@Destination
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navigator: DestinationsNavigator
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val images = state.images.collectAsLazyPagingItems()
    val favoriteImageIds = state.favoritesImageIds

    HomeContent(
        scrollBehavior,
        images = images,
        favoriteImageIds = favoriteImageIds,
        onImageClick = { imageId ->
            navigator.navigate(FullImageScreenDestination(imageId))
        },
        onSearchClick = {
            navigator.navigate(SearchScreenDestination)
        },
        onFABClick = {
            navigator.navigate(FavoritesScreenDestination)
        },
        onACtion = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    scrollBehavior: TopAppBarScrollBehavior,
    images: LazyPagingItems<UnsplashImage>,
    favoriteImageIds: List<String>,
    onImageClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFABClick: () -> Unit,
    onACtion: (HomeAction) -> Unit
) {
    var showImagePreview by remember { mutableStateOf(false) }
    var activeImage by remember { mutableStateOf<UnsplashImage?>(null) }
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()

    Scaffold(
        topBar = {
            ImageSplashAppBar(
                scrollBehavior = scrollBehavior,
                onSearchClick = onSearchClick
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onFABClick() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_save),
                    contentDescription = "Favorites",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface),
        ) {
            ImagesVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                lazyStaggeredGridState = lazyStaggeredGridState,
                images = images,
                favoriteImageIds = favoriteImageIds,
                onImageClick = onImageClick,
                onImageDragStart = { image ->
                    activeImage = image
                    showImagePreview = true
                },
                onImageDragEnd = {
//                    activeImage = null
                    showImagePreview = false },
                onToggleFavoriteStatus = {
                    onACtion(HomeAction.ToggleFavoriteStatus(it))
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