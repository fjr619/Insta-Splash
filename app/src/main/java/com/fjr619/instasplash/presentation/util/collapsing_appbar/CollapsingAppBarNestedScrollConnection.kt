package com.fjr619.instasplash.presentation.util.collapsing_appbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

//https://medium.com/androiddevelopers/understanding-nested-scrolling-in-jetpack-compose-eb57c1ea0af0
class CollapsingAppBarNestedScrollConnection(
    private val appBarMaxHeight: Int
) : NestedScrollConnection {

    var appBarOffset: Int by mutableIntStateOf(0)
        private set

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

        val delta = available.y.toInt()
        val newOffset = appBarOffset + delta
        val previousOffset = appBarOffset
        appBarOffset = newOffset.coerceIn(-appBarMaxHeight, 0)
        val consumed = appBarOffset - previousOffset

        println("appBarOffset $appBarOffset")

        return Offset(0f, consumed.toFloat())
    }
}