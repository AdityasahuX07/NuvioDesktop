package com.nuvio.app.core.ui

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged

/**
 * Global desktop keyboard shortcuts (1-4, 0, F, Backspace) are installed via an AWT-level
 * KeyEventDispatcher that runs before Compose sees the event, so it has no way to ask Compose
 * "is a text field currently focused?". This tracker is the bridge: text inputs that opt in via
 * [reportsDesktopTextInputFocus] increment/decrement this count as they gain/lose focus, and the
 * dispatcher checks [isFocused] before treating one of those keys as a shortcut.
 */
internal object DesktopTextInputFocusTracker {
    @Volatile
    private var focusedCount = 0

    val isFocused: Boolean get() = focusedCount > 0

    fun setFocused(focused: Boolean) {
        focusedCount = (focusedCount + if (focused) 1 else -1).coerceAtLeast(0)
    }
}

internal actual fun Modifier.reportsDesktopTextInputFocus(): Modifier = composed {
    var wasFocused by remember { mutableStateOf(false) }
    DisposableEffect(Unit) {
        onDispose {
            if (wasFocused) {
                DesktopTextInputFocusTracker.setFocused(false)
                wasFocused = false
            }
        }
    }
    onFocusChanged { state ->
        if (state.isFocused != wasFocused) {
            wasFocused = state.isFocused
            DesktopTextInputFocusTracker.setFocused(state.isFocused)
        }
    }
}
