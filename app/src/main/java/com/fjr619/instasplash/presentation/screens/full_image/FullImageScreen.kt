package com.fjr619.instasplash.presentation.screens.full_image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

//data class FullImageScreenNavArgs(
//    val imageId: String,
//)

@RootNavGraph
@Destination(
//    navArgsDelegate = FullImageScreenNavArgs::class
)
@Composable
fun FullImageScreen(
    imageId: String,
    navigator: DestinationsNavigator
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = { navigator.popBackStack() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
        Text(text = "Full Image Screen; ImageId $imageId")
    }
}