package com.fjr619.instasplash.presentation.screens.full_image.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.fjr619.instasplash.R
import com.fjr619.instasplash.domain.model.UnsplashImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullImageTopAppBar(
    image: UnsplashImage?,
    isVisible: Boolean,
    isError: Boolean,
    onBackClick: () -> Unit,
    onPhotographerNameClick: (String) -> Unit,
    onDownloadImgClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable {
                                image?.let { onPhotographerNameClick(it.photographerProfileLink) }
                            },
                        model = image?.photographerProfileImgUrl,
                        contentDescription = null
                    )
                    Column(
                        modifier = Modifier.clickable {
                            image?.let { onPhotographerNameClick(it.photographerProfileLink) }
                        }
                    ) {
                        Text(
                            text = image?.photographerName ?: "",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = image?.photographerUsername ?: "",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

            },
            navigationIcon = {
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go Back"
                    )
                }
            },
            actions = {
                if (!isError) {
                    IconButton(onClick = { onDownloadImgClick() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_download),
                            contentDescription = "Download the image",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

            }
        )

//        Row(
//            modifier = modifier.background(Color.Red),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(onClick = { onBackClick() }) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = "Go Back"
//                )
//            }
//            AsyncImage(
//                modifier = Modifier
//                    .size(30.dp)
//                    .clip(CircleShape),
//                model = image?.photographerProfileImgUrl,
//                contentDescription = null
//            )
//            Spacer(modifier = Modifier.width(10.dp))
//            Column(
//                modifier = Modifier.clickable {
//                    image?.let { onPhotographerNameClick(it.photographerProfileLink) }
//                }
//            ) {
//                Text(
//                    text = image?.photographerName ?: "",
//                    style = MaterialTheme.typography.titleMedium
//                )
//                Text(
//                    text = image?.photographerUsername ?: "",
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
//            Spacer(modifier = Modifier.weight(1f))
//            IconButton(onClick = { onDownloadImgClick() }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_download),
//                    contentDescription = "Download the image",
//                    tint = MaterialTheme.colorScheme.onBackground
//                )
//            }
//        }
    }
}