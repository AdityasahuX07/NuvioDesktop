package com.nuvio.app.core.ui

import androidx.compose.ui.Modifier

/**
 * Marks the node this is applied to as a text input for desktop keyboard-shortcut purposes.
 * While it has focus, the global single-key shortcuts (1-4 tab switch, 0 open search, plain F
 * fullscreen, Backspace back) are suppressed so typing into the field isn't hijacked. No-op on
 * Android/iOS.
 */
internal expect fun Modifier.reportsDesktopTextInputFocus(): Modifier
