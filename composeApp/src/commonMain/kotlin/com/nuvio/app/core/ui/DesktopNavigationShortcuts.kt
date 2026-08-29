package com.nuvio.app.core.ui

/**
 * Registers the callbacks that the global desktop keyboard shortcuts invoke (see
 * `installDesktopNavigationShortcuts` on desktop): digit keys 1-4 switch tabs, 0 opens
 * Search focused, Esc/Backspace go back. No-op on Android/iOS, which have no such
 * shortcuts. Returns a disposer to call when the caller leaves composition.
 */
internal expect fun registerDesktopNavigationShortcutHandlers(
    onSelectTab: (index: Int) -> Unit,
    onOpenSearchFocused: () -> Unit,
): () -> Unit

/**
 * Registers the callback that catches the very first arrow-key press before anything in the
 * current screen has real Compose focus (see [com.nuvio.app.core.ui.focus.DpadNavActivation] for
 * why this is needed). [onArrowKeyPressed] should return true if it activated navigation for that
 * press (so the platform layer swallows the key event) or false to let the key event continue
 * through to Compose's normal dispatch. No-op on Android/iOS, which don't have this "nothing
 * focused yet" problem (D-pad/touch already routes through Compose's own focus system there).
 * Returns a disposer to call when the caller leaves composition.
 */
internal expect fun registerDesktopArrowKeyActivationHandler(
    onArrowKeyPressed: (com.nuvio.app.core.ui.focus.NavArrowKeyDirection) -> Boolean,
): () -> Unit
