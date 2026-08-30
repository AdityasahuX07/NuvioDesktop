package com.nuvio.app.features.home.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester

/**
 * Coordinates keyboard/D-pad focus transitions between the Home screen's Hero section and the
 * first poster row beneath it (Up from first poster row -> Hero "View Details"; Down from Hero
 * -> first/previously-focused poster). Scoped to a single Home screen composition (created with
 * `remember` in [com.nuvio.app.features.home.HomeScreen]) - this is not a global index system,
 * just a small named slot for the one cross-section wiring Compose's spatial focus search can't
 * infer on its own (Hero and the first row aren't visually stacked in a way that always resolves
 * correctly, and we want the *previously focused* poster restored, not always the first).
 */
class HomeFocusCoordinator {
    /** Focus target for the Hero's "View Details" button/pill. */
    val heroViewDetailsFocusRequester = FocusRequester()

    /** Focus target for the Fullscreen action button. */
    val fullscreenButtonFocusRequester = FocusRequester()

    /** Focus target for the first poster of the first poster row. */
    val firstPosterFocusRequester = FocusRequester()

    /**
     * True while the Hero "View Details" button has real keyboard/D-pad focus. The hero carousel
     * auto-rotates on a timer regardless of focus state, and each page is a *separate* composable
     * instance (only the currently active page ever wires up [heroViewDetailsFocusRequester], to
     * avoid the same requester being attached to multiple simultaneously-rendered pages - see
     * `DesktopHeroContentBlock`). If auto-rotation fires while the button is focused, the
     * underlying focused node goes away mid-interaction and the ring/scale stop responding. Home's
     * hero pager reads this to pause auto-rotation for as long as the button holds real focus.
     */
    var isHeroButtonFocused = mutableStateOf(false)

    /** True when actively transferring focus between hero pages. */
    var isHeroFocusTransferring = false

    /**
     * True once keyboard/D-pad navigation has been activated for the current visit to Home, so a
     * later arrow press doesn't re-steal focus on every recomposition. Home stays composed even
     * while another tab is showing (see App.kt), so this must be reset whenever Home becomes the
     * active tab again - switching tabs (especially via mouse click, which moves real Compose
     * focus onto the clicked tab button) leaves this app-wide instance's flag `true` from a
     * previous visit while no Home poster actually holds focus anymore, silently breaking arrow
     * keys until something else (like opening and closing a details screen) happens to restore
     * real focus. HomeScreen resets this via a `LaunchedEffect(homeSelected)`.
     */
    var hasRequestedInitialFocus = false

    /** The most recently focused poster's FocusRequester, restored on Down-from-Hero when set. */
    var lastFocusedPosterFocusRequester: FocusRequester? = null

    fun downFromHeroTarget(): FocusRequester =
        lastFocusedPosterFocusRequester ?: firstPosterFocusRequester
}

val LocalHomeFocusCoordinator = compositionLocalOf<HomeFocusCoordinator?> { null }

@Composable
fun rememberHomeFocusCoordinator(): HomeFocusCoordinator = remember { HomeFocusCoordinator() }
