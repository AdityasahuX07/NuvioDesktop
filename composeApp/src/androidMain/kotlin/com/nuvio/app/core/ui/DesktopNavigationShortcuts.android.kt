package com.nuvio.app.core.ui

internal actual fun registerDesktopNavigationShortcutHandlers(
    onSelectTab: (index: Int) -> Unit,
    onOpenSearchFocused: () -> Unit,
): () -> Unit = {}

internal actual fun registerDesktopArrowKeyActivationHandler(
    onArrowKeyPressed: (com.nuvio.app.core.ui.focus.NavArrowKeyDirection) -> Boolean,
): () -> Unit = {}
