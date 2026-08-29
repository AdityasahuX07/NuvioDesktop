package com.nuvio.app.core.ui

import com.nuvio.app.core.ui.focus.NavArrowKeyDirection
import java.awt.KeyEventDispatcher
import java.awt.KeyboardFocusManager
import java.awt.event.KeyEvent

private object DesktopAppNavigationShortcuts {
    private var tabSelectHandler: ((Int) -> Unit)? = null
    private var openSearchHandler: (() -> Unit)? = null
    private var arrowKeyActivationHandler: ((NavArrowKeyDirection) -> Boolean)? = null

    fun setHandlers(
        onSelectTab: (Int) -> Unit,
        onOpenSearchFocused: () -> Unit,
    ): () -> Unit {
        tabSelectHandler = onSelectTab
        openSearchHandler = onOpenSearchFocused
        return {
            if (tabSelectHandler === onSelectTab) tabSelectHandler = null
            if (openSearchHandler === onOpenSearchFocused) openSearchHandler = null
        }
    }

    fun setArrowKeyActivationHandler(handler: (NavArrowKeyDirection) -> Boolean): () -> Unit {
        arrowKeyActivationHandler = handler
        return { if (arrowKeyActivationHandler === handler) arrowKeyActivationHandler = null }
    }

    fun selectTab(index: Int) {
        tabSelectHandler?.invoke(index)
    }

    fun openSearchFocused() {
        openSearchHandler?.invoke()
    }

    /** Returns true if a screen activated navigation for this press (so the caller should consume it). */
    fun activateArrowKeyNav(direction: NavArrowKeyDirection): Boolean =
        arrowKeyActivationHandler?.invoke(direction) ?: false
}

internal actual fun registerDesktopNavigationShortcutHandlers(
    onSelectTab: (index: Int) -> Unit,
    onOpenSearchFocused: () -> Unit,
): () -> Unit = DesktopAppNavigationShortcuts.setHandlers(onSelectTab, onOpenSearchFocused)

internal actual fun registerDesktopArrowKeyActivationHandler(
    onArrowKeyPressed: (NavArrowKeyDirection) -> Boolean,
): () -> Unit = DesktopAppNavigationShortcuts.setArrowKeyActivationHandler(onArrowKeyPressed)

/**
 * Installs the global (AWT-level, pre-Compose) shortcuts for tab switching, opening search,
 * and back navigation - the same mechanism [installDesktopAppFullscreenShortcuts] uses for
 * F11/F, chosen for the same reason: it fires regardless of Compose's internal focus state, so
 * it works immediately on app start (unlike a Compose onKeyEvent modifier, which only receives
 * events once something in the tree has focus) and keeps working across native fullscreen
 * focus changes. Install once at the window level (see Main.kt).
 *
 * This is also where the very first arrow-key press of a session gets caught: the app
 * deliberately never grabs Compose focus on screen mount (so no focus ring/border shows until
 * the user actually presses an arrow key), which means Compose has nothing to dispatch the very
 * first arrow-key press through. [DesktopAppNavigationShortcuts.activateArrowKeyNav] hands that
 * first press to the active screen so it can request focus onto its own initial target; every
 * press after that is handled entirely by normal Compose focus dispatch, since something now has
 * real focus, so this dispatcher steps out of the way (returns false) once activation succeeds.
 */
internal fun installDesktopNavigationShortcuts(): () -> Unit {
    val dispatcher = KeyEventDispatcher { event ->
        if (event.id != KeyEvent.KEY_PRESSED) return@KeyEventDispatcher false
        when {
            event.isDesktopBackShortcut() -> {
                if (event.keyCode == KeyEvent.VK_BACK_SPACE) {
                    event.keyCode = KeyEvent.VK_ESCAPE
                }
                triggerDesktopBack()
            }
            event.isDesktopTabSwitchShortcut() -> {
                DesktopAppNavigationShortcuts.selectTab(event.tabSwitchIndex())
                true
            }
            event.isDesktopOpenSearchShortcut() -> {
                DesktopAppNavigationShortcuts.openSearchFocused()
                true
            }
            event.isDesktopArrowKeyActivationCandidate() -> {
                val direction = event.arrowKeyDirection()
                direction != null && DesktopAppNavigationShortcuts.activateArrowKeyNav(direction)
            }
            else -> false
        }
    }
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher)
    return {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher)
    }
}

private fun KeyEvent.hasNoShortcutModifiers(): Boolean =
    modifiersEx and (KeyEvent.CTRL_DOWN_MASK or KeyEvent.ALT_DOWN_MASK or KeyEvent.META_DOWN_MASK) == 0

private fun KeyEvent.isDesktopBackShortcut(): Boolean {
    if (keyCode == KeyEvent.VK_ESCAPE) return true
    if (keyCode != KeyEvent.VK_BACK_SPACE) return false
    // Backspace must not steal focus away from a text field mid-edit.
    return !DesktopTextInputFocusTracker.isFocused
}

private val TAB_SWITCH_KEYCODES = setOf(
    KeyEvent.VK_1, KeyEvent.VK_NUMPAD1,
    KeyEvent.VK_2, KeyEvent.VK_NUMPAD2,
    KeyEvent.VK_3, KeyEvent.VK_NUMPAD3,
    KeyEvent.VK_4, KeyEvent.VK_NUMPAD4,
)

private fun KeyEvent.isDesktopTabSwitchShortcut(): Boolean {
    if (DesktopTextInputFocusTracker.isFocused) return false
    if (!hasNoShortcutModifiers()) return false
    return keyCode in TAB_SWITCH_KEYCODES
}

private fun KeyEvent.tabSwitchIndex(): Int = when (keyCode) {
    KeyEvent.VK_1, KeyEvent.VK_NUMPAD1 -> 1
    KeyEvent.VK_2, KeyEvent.VK_NUMPAD2 -> 2
    KeyEvent.VK_3, KeyEvent.VK_NUMPAD3 -> 3
    KeyEvent.VK_4, KeyEvent.VK_NUMPAD4 -> 4
    else -> -1
}

private fun KeyEvent.isDesktopOpenSearchShortcut(): Boolean {
    if (DesktopTextInputFocusTracker.isFocused) return false
    if (!hasNoShortcutModifiers()) return false
    // "/" mirrors the common browser/app convention (Google, GitHub, etc.) for jumping to search.
    return keyCode == KeyEvent.VK_0 || keyCode == KeyEvent.VK_NUMPAD0 || keyCode == KeyEvent.VK_SLASH
}

private val ARROW_KEYCODES = setOf(
    KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
)

private fun KeyEvent.isDesktopArrowKeyActivationCandidate(): Boolean {
    // Never steal an arrow key from an active text field/slider/dropdown - let it handle its
    // own cursor movement or value change.
    if (DesktopTextInputFocusTracker.isFocused) return false
    if (!hasNoShortcutModifiers()) return false
    return keyCode in ARROW_KEYCODES
}

private fun KeyEvent.arrowKeyDirection(): NavArrowKeyDirection? = when (keyCode) {
    KeyEvent.VK_UP -> NavArrowKeyDirection.Up
    KeyEvent.VK_DOWN -> NavArrowKeyDirection.Down
    KeyEvent.VK_LEFT -> NavArrowKeyDirection.Left
    KeyEvent.VK_RIGHT -> NavArrowKeyDirection.Right
    else -> null
}
