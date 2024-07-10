package com.fjr619.instasplash.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fjr619.instasplash.R
import com.fjr619.instasplash.domain.model.UnsplashImage
import com.fjr619.instasplash.presentation.components.ImageSplashAppBar
import com.fjr619.instasplash.presentation.components.ImagesVerticalGrid
import com.fjr619.instasplash.presentation.theme.InstaSplashTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(true)
@Destination
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    HomeContent(
        scrollBehavior,
        images = viewModel.images,
        onImageClick = {},
        onSearchClick = {},
        onFABClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    scrollBehavior: TopAppBarScrollBehavior,
    images: List<UnsplashImage>,
    onImageClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFABClick: () -> Unit,
) {

    Scaffold(
        topBar = {
            ImageSplashAppBar(
                scrollBehavior = scrollBehavior,
                onSearchClick = onSearchClick
            )
        },
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize().padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface),
        ) {
            ImagesVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                images = images,
                onImageClick = onImageClick
            )

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                onClick = { onFABClick() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_save),
                    contentDescription = "Favorites",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HomeContentPreview() {
    InstaSplashTheme {
        HomeContent(
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            images = listOf(),
            onImageClick = {},
            onSearchClick = {},
            onFABClick = {}
        )
    }
}