package com.fjr619.instasplash.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.fjr619.instasplash.presentation.screens.favorite.FavoritesScreen
import com.fjr619.instasplash.presentation.screens.favorite.FavoritesViewModel
import com.fjr619.instasplash.presentation.screens.full_image.FullImageScreen
import com.fjr619.instasplash.presentation.screens.full_image.FullImageViewModel
import com.fjr619.instasplash.presentation.screens.home.HomeScreen
import com.fjr619.instasplash.presentation.screens.home.HomeViewModel
import com.fjr619.instasplash.presentation.screens.profile.ProfileScreen
import com.fjr619.instasplash.presentation.screens.search.SearchEvent
import com.fjr619.instasplash.presentation.screens.search.SearchScreen
import com.fjr619.instasplash.presentation.screens.search.SearchViewModel
import com.fjr619.instasplash.presentation.util.directional_lazy_scrollable_state.ScrollDirection
import com.fjr619.instasplash.presentation.util.directional_lazy_scrollable_state.rememberDirectionalLazyScrollableState
import com.fjr619.instasplash.presentation.util.rememberWindowInsetsController
import com.fjr619.instasplash.presentation.util.snackbar.AppSnackbarVisual
import com.fjr619.instasplash.presentation.util.snackbar.LocalSnackbarController
import com.fjr619.instasplash.presentation.util.snackbar.SnackbarController
import com.fjr619.instasplash.presentation.util.snackbar.SnackbarMessageHandler
import com.fjr619.instasplash.presentation.util.toggleStatusBars
import com.fjr619.studyfocus.presentation.util.ObserveAsEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraphSetup(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen
    ) {
        composable<Routes.HomeScreen> {

            val viewModel: HomeViewModel = koinViewModel()
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val images = viewModel.images.collectAsLazyPagingItems()
            val favoriteImageIds = state.favoritesImageIds

            HomeScreen(
                scrollBehavior,
                images = images,
                favoriteImageIds = favoriteImageIds,
                onImageClick = { imageId ->
                    navController.navigate(Routes.FullImageScreen(imageId))
                },
                onSearchClick = {
                    navController.navigate(Routes.SearchScreen)
                },
                onFABClick = {
                    navController.navigate(Routes.FavoritesScreen)
                },
                onACtion = viewModel::onAction
            )
        }

        composable<Routes.SearchScreen> {
            val viewModel: SearchViewModel = koinViewModel()
            val coroutineScope = rememberCoroutineScope()
            val state by viewModel.searchState.collectAsStateWithLifecycle()
            val lazyStaggeredGridState = rememberLazyStaggeredGridState()
            val directionalLazyStaggeredGridState = rememberDirectionalLazyScrollableState(
                lazyStaggeredGridState.firstVisibleItemIndex,
                lazyStaggeredGridState.firstVisibleItemScrollOffset
            )

            directionalLazyStaggeredGridState.observe(
                lazyStaggeredGridState.isScrollInProgress,
                lazyStaggeredGridState.firstVisibleItemIndex,
                lazyStaggeredGridState.firstVisibleItemScrollOffset
            )

            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current
            val keyboardController = LocalSoftwareKeyboardController.current

            ObserveAsEvents(flow = viewModel.events) { event ->
                when (event) {

                    //scroll ke item 0 ketika sukses request searc image
                    SearchEvent.DoScrollUp -> {
                        coroutineScope.launch {
                            lazyStaggeredGridState.scrollToItem(0)
                        }
                    }
                }
            }

            LaunchedEffect(key1 = lazyStaggeredGridState.isScrollInProgress) {
                if (lazyStaggeredGridState.isScrollInProgress) {
                    keyboardController?.hide()
                }
            }

            LaunchedEffect(key1 = directionalLazyStaggeredGridState.scrollDirection) {
                if (directionalLazyStaggeredGridState.scrollDirection == ScrollDirection.Up) {
                    delay(100)
                    focusManager.clearFocus()
                }
            }

            SearchScreen(
                state = state,
                lazyStaggeredGridState = lazyStaggeredGridState,
                focusRequester = focusRequester,
                focusManager = focusManager,
                keyboardController = keyboardController,
                onAction = viewModel::onAction,
                onImageClick = { imageId ->
                    navController.navigate(Routes.FullImageScreen(imageId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<Routes.FullImageScreen>(
            deepLinks = listOf(
                navDeepLink {
                    //example deeplink -> adb shell am start -W -a android.intent.action.VIEW -d "example://compose/image/fag7vJEpUFM"
                    uriPattern = "example://compose/image/{imageId}"
                }
            )
        ) { entry ->
            println("data ${entry.toRoute<Routes.FullImageScreen>().imageId}")
            val viewModel: FullImageViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val snackbarController: SnackbarController = LocalSnackbarController.current
            val windowInsetsController = rememberWindowInsetsController()
            var showBars by rememberSaveable { mutableStateOf(false) }

            SnackbarMessageHandler(
                snackbarMessage = state.snackbarMessage,
                onDismissSnackbar = viewModel::dismissSnackbar
            )

            LaunchedEffect(key1 = showBars) {
                windowInsetsController.toggleStatusBars(show = showBars)
            }

            BackHandler(enabled = !showBars) {
                windowInsetsController.toggleStatusBars(show = true)
                navController.popBackStack()
            }

            FullImageScreen(
                state = state,
                showBars = showBars,
                onBackClick = { navController.popBackStack() },
                onPhotographerNameClick = { profileLink ->
                    navController.navigate(Routes.ProfileScreen(profileLink))
                },
                onImageDownloadClick = { url, title ->
                    viewModel.downloadImage(url, title)
                    snackbarController.showMessage(
                        snackbarVisuals = AppSnackbarVisual(
                            message = "Downloading ...",
                        )
                    )
                },
                updateShowbars = { value ->
                    showBars = value
                },
                toggleStatusBars = {
                    windowInsetsController.toggleStatusBars(showBars)
                }
            )
        }

        composable<Routes.ProfileScreen> { entry ->
            val profileLink = entry.toRoute<Routes.ProfileScreen>().profileLink
            ProfileScreen(
                profileLink = profileLink,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<Routes.FavoritesScreen> {
            val viewModel: FavoritesViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val favoriteImages = viewModel.images.collectAsLazyPagingItems()
            val favoriteImageIds = state.favoritesImageIds
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

            FavoritesScreen(
                favoriteImages = favoriteImages,
                favoriteImageIds = favoriteImageIds,
                scrollBehavior = scrollBehavior,
                onImageClick = { imageId ->
                    navController.navigate(Routes.FullImageScreen(imageId))
                },
                onSearchClick = {
                    navController.navigate(Routes.SearchScreen)
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onACtion = viewModel::onAction
            )
        }
    }
}