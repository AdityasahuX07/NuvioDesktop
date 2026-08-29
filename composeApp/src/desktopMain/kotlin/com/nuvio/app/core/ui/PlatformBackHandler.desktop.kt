package com.nuvio.app.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

/**
 * Desktop has no OS-level back gesture/button, so PlatformBackHandler here maintains
 * its own stack of active handlers, mirroring androidx.activity.compose.BackHandler on
 * Android: every composition of PlatformBackHandler registers an entry, and the most
 * recently registered *enabled* entry is the one invoked when back is triggered (e.g.
 * by the Esc/Backspace shortcut). This lets existing call sites throughout the app
 * (player panels, editor screens, dialogs, the root tab screen, etc.) work unmodified.
 */
private object DesktopBackDispatcher {
    private val entries = mutableListOf<DesktopBackHandlerEntry>()

    fun register(entry: DesktopBackHandlerEntry) {
        entries.add(entry)
    }

    fun unregister(entry: DesktopBackHandlerEntry) {
        entries.remove(entry)
    }

    /** Invokes the topmost enabled handler, if any. Returns true if something handled it. */
    fun dispatchBack(): Boolean {
        val entry = entries.lastOrNull { it.enabled() } ?: return false
        entry.onBack()
        return true
    }
}

private class DesktopBackHandlerEntry(
    val enabled: () -> Boolean,
    val onBack: () -> Unit,
)

@Composable
actual fun PlatformBackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
) {
    val currentEnabled = rememberUpdatedState(enabled)
    val currentOnBack = rememberUpdatedState(onBack)
    val entry = remember {
        DesktopBackHandlerEntry(
            enabled = { currentEnabled.value },
            onBack = { currentOnBack.value() },
        )
    }
    DisposableEffect(entry) {
        DesktopBackDispatcher.register(entry)
        onDispose { DesktopBackDispatcher.unregister(entry) }
    }
}

/** Triggers the topmost enabled [PlatformBackHandler] on desktop. Returns true if handled. */
internal fun triggerDesktopBack(): Boolean = DesktopBackDispatcher.dispatchBack()
