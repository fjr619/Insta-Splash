package com.fjr619.instasplash.presentation.screens.search

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.fjr619.instasplash.domain.model.UnsplashImage
import com.fjr619.instasplash.presentation.components.ImagesVerticalGrid
import com.fjr619.instasplash.presentation.components.PreviewImageCard
import com.fjr619.instasplash.presentation.util.animated_placeholder.AnimatedPlaceholder
import com.fjr619.instasplash.presentation.util.collapsing_appbar.rememberCollapsingAppBarStateHolder
import com.fjr619.instasplash.presentation.util.searchKeywords


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchState,
    lazyStaggeredGridState: LazyStaggeredGridState,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?,
    onAction: (SearchAction) -> Unit,
    onImageClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var isSuggestionChipsVisible by remember { mutableStateOf(false) }

    var showImagePreview by remember { mutableStateOf(false) }
    var activeImage by remember { mutableStateOf<UnsplashImage?>(null) }

    val collapsingAppBarStateHolder = rememberCollapsingAppBarStateHolder(LocalDensity.current)
    val connection = collapsingAppBarStateHolder.getConnection()
    val spaceHeight by collapsingAppBarStateHolder.getSpaceHeight(connection.appBarOffset)

    val searchImages = state.images.collectAsLazyPagingItems()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection)
    ) {
        if (state.searchQuery.isNotEmpty()) {
            Column {
                Spacer(modifier = Modifier.height(spaceHeight))
                ImagesVerticalGrid(
                    lazyStaggeredGridState = lazyStaggeredGridState,
                    images = searchImages,
                    favoriteImageIds = state.favoritesImageIds,
                    loadState = searchImages.loadState,
                    onImageClick = onImageClick,
                    onImageDragStart = { image ->
                        activeImage = image
                        showImagePreview = true
                    },
                    onImageDragEnd = { showImagePreview = false },
                    onToggleFavoriteStatus = {
                        onAction(SearchAction.ToggleFavoriteStatus(it))
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(0, connection.appBarOffset)
                }
                .onSizeChanged {
                    collapsingAppBarStateHolder.appBarMaxHeightPx = (it.height)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .padding(bottom = 10.dp)
                    .onFocusChanged { isSuggestionChipsVisible = it.isFocused },
                query = state.searchQuery,
                onQueryChange = {
                    println("query $it")
                    onAction(SearchAction.SearchQueryChanged(it))
                },
                onSearch = {
                    onAction(SearchAction.Search)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                placeholder = {
                    AnimatedPlaceholder(
                        hints = listOf(
                            "Search portrait images",
                            "Search landscape images",
                            "Search nature images",
                            "Search travel images",
                            "Search food images",
                            "Search animal images"
                        )
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (state.searchQuery.isNotEmpty())
                                onAction(SearchAction.SearchQueryChanged(""))
                            else {
                                onBackClick()
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }

                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                    }
                },
                active = false,
                onActiveChange = {},
                content = {}
            )



            AnimatedVisibility(visible = isSuggestionChipsVisible) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(searchKeywords) { keyword ->
                        SuggestionChip(
                            onClick = {
                                onAction(SearchAction.SearchQueryChanged(keyword))
                                onAction(SearchAction.Search)

                                keyboardController?.hide()
                                focusManager.clearFocus()
                            },
                            label = { Text(text = keyword) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }
        }

        PreviewImageCard(
            modifier = Modifier.padding(20.dp),
            isVisible = showImagePreview,
            image = activeImage
        )
    }
}