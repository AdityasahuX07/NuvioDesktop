package com.nuvio.app.core.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.dpadFocusable(
    interactionSource: MutableInteractionSource? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(12.dp),
    borderWidth: Dp = 3.dp,
    scaleOnFocus: Float = 1.05f
) = composed {
    val actualInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isFocused by actualInteractionSource.collectIsFocusedAsState()

    this.then(
        Modifier
            .graphicsLayer {
                if (isFocused) {
                    scaleX = scaleOnFocus
                    scaleY = scaleOnFocus
                }
            }
            .border(
                width = if (isFocused) borderWidth else 0.dp,
                color = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = shape
            )
            .focusable(enabled = enabled, interactionSource = actualInteractionSource)
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyUp && (event.key == Key.Enter || event.key == Key.NumPadEnter || event.key == Key.Spacebar)) {
                    onClick?.invoke()
                    true
                } else {
                    false
                }
            }
    )
}
