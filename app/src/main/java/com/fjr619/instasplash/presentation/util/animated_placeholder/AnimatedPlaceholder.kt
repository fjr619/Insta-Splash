package com.fjr619.instasplash.presentation.util.animated_placeholder

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private suspend fun <T> ListIterator<T>.doWhenHasNextOrPrevious(
    delayMillis: Long = 2000,
    doWork: suspend (T) -> Unit
) {
    while (hasNext() || hasPrevious()) {
        while (hasNext()) {
            delay(delayMillis)
            doWork(next())
        }

        while (hasPrevious()) {
            delay(delayMillis)
            doWork(previous())
        }
    }
}

private object ScrollAnimation {
    operator fun invoke(): ContentTransform {
        return slideInVertically(
            initialOffsetY = { 50 },
            animationSpec = tween()
        ) + fadeIn() togetherWith slideOutVertically(
            targetOffsetY = { -50 },
            animationSpec = tween()
        ) + fadeOut()
    }
}

//https://medium.com/proandroiddev/animated-placeholder-with-jetpack-compose-60c85547b47a
@Composable
fun AnimatedPlaceholder(
    hints: List<String>,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {

    val iterator = hints.listIterator()

    val target by produceState(initialValue = hints.first()) {
        iterator.doWhenHasNextOrPrevious {
            value = it
        }
    }

    AnimatedContent(
        targetState = target,
        transitionSpec = { ScrollAnimation() },
        label = ""
    ) { str ->
        Text(text = str, color = textColor, fontSize = 14.sp)
    }
}

