package com.fjr619.instasplash.presentation.util.directional_lazy_scrollable_state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

//https://stackoverflow.com/a/77198230
@Stable
class DirectionalLazyScrollableState(
    private val firstVisibleItemIndex: Int,
    private val firstVisibleItemScrollOffset: Int,
    private val coroutineScope: CoroutineScope
) {
    private var positionY = firstVisibleItemScrollOffset
    private var visibleItem = firstVisibleItemIndex

    private var currentTime = System.currentTimeMillis()
    var scrollDirection by mutableStateOf(ScrollDirection.None)

    init {

        coroutineScope.launch {
            while (isActive) {
                delay(120)
                if (System.currentTimeMillis() - currentTime > 120) {
                    scrollDirection = ScrollDirection.None
                }
            }
        }
    }

    fun observe(
        isScrollInProgress: Boolean,
        firstVisibleItemIndex: Int,
        firstVisibleItemScrollOffset: Int
    ) {
        snapshotFlow {
            val scrollInt = if (isScrollInProgress) 20000 else 10000
            val visibleItemInt = firstVisibleItemIndex * 10
            scrollInt + visibleItemInt + firstVisibleItemScrollOffset
        }
            .onEach {
                if (isScrollInProgress.not()) {
                    scrollDirection = ScrollDirection.None
                } else {

                    currentTime = System.currentTimeMillis()

                    // We are scrolling while first visible item hasn't changed yet
                    if (firstVisibleItemIndex == visibleItem) {
                        val direction = if (firstVisibleItemScrollOffset > positionY) {
                            ScrollDirection.Down
                        } else {
                            ScrollDirection.Up
                        }
                        positionY = firstVisibleItemScrollOffset

                        scrollDirection = direction
                    } else {

                        val direction = if (firstVisibleItemIndex > visibleItem) {
                            ScrollDirection.Down
                        } else {
                            ScrollDirection.Up
                        }
                        positionY = firstVisibleItemScrollOffset
                        visibleItem = firstVisibleItemIndex
                        scrollDirection = direction
                    }
                }
            }.launchIn(coroutineScope)
    }
}

@Composable
fun rememberDirectionalLazyScrollableState(
    firstVisibleItemIndex: Int,
    firstVisibleItemScrollOffset: Int
): DirectionalLazyScrollableState {

    val coroutineScope = rememberCoroutineScope()

    return remember {
        DirectionalLazyScrollableState(
            firstVisibleItemIndex,
            firstVisibleItemScrollOffset,
            coroutineScope)
    }
}