package com.fjr619.instasplash.presentation.util.snackbar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun AppSnackbar(
    data: SnackbarData
) {
    val isError = (data.visuals as? AppSnackbarVisual)?.isError ?: false

    Snackbar(
        modifier = Modifier.padding(16.dp).clip(RoundedCornerShape(0.dp)),
        containerColor = if (isError)
            MaterialTheme.colorScheme.error
        else
            MaterialTheme.colorScheme.onSurface,
        action = {
            data.visuals.actionLabel?.let {
                TextButton(onClick = { data.performAction() }) {
                    Text(
                        text = it, style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isError)
                                MaterialTheme.colorScheme.onError
                            else
                                MaterialTheme.colorScheme.onTertiary
                        )
                    )
                }
            }
        },
    )

    {
        Text(
            text = data.visuals.message,
            style = MaterialTheme.typography.labelSmall
        )
    }
}