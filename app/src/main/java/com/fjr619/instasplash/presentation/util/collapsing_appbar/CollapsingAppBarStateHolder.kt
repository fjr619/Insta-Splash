package com.fjr619.instasplash.presentation.util.collapsing_appbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

class CollapsingAppBarStateHolder(
    val density: Density,
) {
    var appBarMaxHeightPx by mutableIntStateOf(0)

    @Composable
    fun getConnection(): CollapsingAppBarNestedScrollConnection {
        return remember(appBarMaxHeightPx) {
            CollapsingAppBarNestedScrollConnection(appBarMaxHeightPx)
        }
    }

    @Composable
    fun getSpaceHeight(appBarOffset: Int): State<Dp> {
        return remember(key1 = density, key2 = appBarMaxHeightPx, key3 = appBarOffset) {
            derivedStateOf {
                with(density) {
                    (appBarMaxHeightPx + appBarOffset).toDp()
                }
            }
        }
    }
}

@Composable
fun rememberCollapsingAppBarStateHolder(
    density: Density
): CollapsingAppBarStateHolder {
    return remember {
        CollapsingAppBarStateHolder(
            density
        )
    }
}