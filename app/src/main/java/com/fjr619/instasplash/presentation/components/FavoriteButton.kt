package com.fjr619.instasplash.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    FilledIconToggleButton(
        modifier = modifier.padding(5.dp).size(24.dp),
        checked = isFavorite,
        onCheckedChange = { onClick() },
        colors = IconButtonDefaults.filledIconToggleButtonColors(
            checkedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = Color.White,
            checkedContentColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        if (isFavorite) {
            Icon(modifier = Modifier.size(16.dp), imageVector = Icons.Default.Favorite, contentDescription = null)
        } else {
            Icon(modifier = Modifier.size(16.dp), imageVector = Icons.Default.FavoriteBorder, contentDescription = null)
        }
    }
}