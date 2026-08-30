package com.nuvio.app.core.ui.focus

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.foundation.focusGroup
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

/**
 * Shared scale applied to a focused (keyboard/D-pad) item - matches the existing desktop
 * mouse-hover poster scale (`DesktopPosterHoverScale` in ShelfComponents.kt) so focus and hover
 * feel like the same interaction language.
 */
private const val DpadFocusScale = 1.045f

/** Default visible gap between a focused item's content and its focus border. */
val DpadFocusGap = 4.dp

/**
 * Draws the app-wide keyboard/D-pad focus indicator: a theme-accent-colored border plus a
 * slight scale bump (matching the desktop hover scale by default). Never uses a hardcoded color -
 * always [MaterialTheme.colorScheme.primary], which resolves to the user's selected accent color
 * (see AppTheme/ThemeColors).
 *
 * The border is drawn with [androidx.compose.ui.graphics.drawscope.Stroke] inset by half its own
 * width, so the stroke never extends past this modifier's own layout bounds (unlike
 * `Modifier.border`, which centers the stroke on the boundary and bleeds half its width outside).
 * This matters specifically because these focusable items usually live inside a `LazyRow`/
 * `LazyColumn`, which clips each item to its own laid-out bounds for virtualization - a
 * bleeding-outside border (or, at large scale values, the scaled content itself) gets its rounded
 * corners cropped by that clip, which is what makes a round-cornered poster's focus ring look
 * squared-off and cut short. Keeping the stroke fully inside bounds removes that source of
 * clipping; the (default, hover-matching) scale bump is the same one already used for mouse hover
 * on these items, which does not get clipped in practice.
 *
 * Place this modifier *before* any `Modifier.clip(...)` in the chain (i.e. earlier/outer) so nothing
 * else clips the border either. Pair with [dpadFocusGapPadding] (placed *after* this but *before*
 * the clip) on the same [interactionSource] to get a visible gap between the border and the
 * clipped content instead of a flush-against-the-edge ring.
 *
 * This is intentionally decoupled from hover/pressed states: it only reacts to [interactionSource]
 * focus events, so mouse/touch interaction is completely unaffected.
 *
 * @param cornerRadius Corner radius to match against the focused content's own shape (e.g. the
 *   same value passed to that content's `Modifier.clip(RoundedCornerShape(cornerRadius))`), so the
 *   ring reads as a concentric, same-roundness frame around it rather than a mismatched shape.
 *   Pass a large value (e.g. [NuvioTokens.Radius.full]) for a circular/pill ring around a square
 *   or pill-shaped target - it's clamped to the target's own size like any rounded-rect radius.
 * @param color Overrides the ring color for this one call site instead of the default
 *   [MaterialTheme.colorScheme.primary]. Leave `null` unless the content underneath can itself
 *   render in the accent color when active/selected (e.g. a toggle button that turns white/accent
 *   when "on") - in that specific case the default ring becomes invisible against its own content,
 *   and the call site should pass a color that contrasts with *that* state instead (e.g. the same
 *   neutral tone the button uses in its "off" state).
 * @param gap Must match the `gap` passed to a paired [dpadFocusGapPadding] on the same
 *   [interactionSource], if any (defaults to `0.dp`, i.e. no pairing - the border sits flush
 *   against this modifier's own bounds). Passing a mismatched value here throws off the
 *   concentric-radius math above, making the ring look flatter or rounder than the content it's
 *   framing.
 */
@Composable
fun Modifier.dpadFocusRing(
    interactionSource: InteractionSource,
    cornerRadius: Dp = 12.dp,
    scale: Boolean = true,
    scaleFactor: Float = DpadFocusScale,
    gap: Dp = 0.dp,
    color: Color? = null,
): Modifier {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val borderWidth by animateDpAsState(
        targetValue = if (isFocused) 3.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "dpad_focus_border_width",
    )
    val scaleValue by animateFloatAsState(
        targetValue = if (isFocused && scale) scaleFactor else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "dpad_focus_scale",
    )
    val accent = color ?: MaterialTheme.colorScheme.primary
    return this
        .then(
            if (isFocused || scaleValue != 1f) {
                Modifier
                    .graphicsLayer {
                        scaleX = scaleValue
                        scaleY = scaleValue
                    }
                    .zIndex(2f)
            } else {
                Modifier
            },
        )
        .then(
            if (borderWidth > 0.dp) {
                Modifier.drawWithContent {
                    drawContent()
                    val strokeWidthPx = borderWidth.toPx()
                    val inset = strokeWidthPx / 2f
                    // The border is drawn on this (outer, ungapped) box while the actual content
                    // sits inset by `gap` (see dpadFocusGapPadding) - offsetting a rounded rect
                    // outward by `gap` increases its true corner radius by exactly `gap`, so add
                    // it here to keep the ring concentric with (same roundness as) the content
                    // it's framing, instead of looking flatter/squarer than the poster underneath.
                    val cornerPx = (cornerRadius.toPx() + gap.toPx() - inset).coerceAtLeast(0f)
                    drawRoundRect(
                        color = accent,
                        topLeft = Offset(inset, inset),
                        size = Size(
                            width = (size.width - strokeWidthPx).coerceAtLeast(0f),
                            height = (size.height - strokeWidthPx).coerceAtLeast(0f),
                        ),
                        cornerRadius = CornerRadius(cornerPx, cornerPx),
                        style = Stroke(width = strokeWidthPx),
                    )
                }
            } else {
                Modifier
            },
        )
}

/**
 * Height (in px) of a persistent overlay - typically a `stickyHeader { }` - that visually covers
 * the top of a scrollable screen without shrinking its reported viewport. `BringIntoViewRequester`
 * only knows about the scrollable's own declared viewport size; it has no idea a sticky header is
 * drawn on top of the first N pixels of it, so its default "scroll this into view" calculation can
 * still land content directly underneath that overlay. Screens with such a header (Search,
 * Library) measure its actual height and provide it here; poster cards read it to pad their
 * bring-into-view requests with that much extra headroom above them.
 */
val LocalStickyHeaderInsetPx = compositionLocalOf { 0 }

/**
 * Extra breathing room (in dp) added on top of a measured sticky header's own height, so a
 * focused poster's border sits with a visible gap below the header instead of butting flush (or
 * slightly clipped) against it. Screens providing [LocalStickyHeaderInsetPx] should add this,
 * converted to px via [LocalDensity], to the header's raw measured height.
 */
val StickyHeaderExtraGap = 14.dp

/**
 * Builds a [Rect] (in the poster's own local coordinates) that's padded upward by
 * [LocalStickyHeaderInsetPx], for use with `BringIntoViewRequester.bringIntoView(rect)`. Passing
 * this instead of the default (whole-node) rect makes the resulting scroll reveal that much extra
 * space above the poster, so a persistent sticky header never ends up covering it.
 */
@Composable
fun rememberStickyHeaderAwareBringIntoViewRect(sizeProvider: () -> Size): () -> Rect {
    val insetPx = LocalStickyHeaderInsetPx.current
    return {
        val size = sizeProvider()
        Rect(left = 0f, top = -insetPx.toFloat(), right = size.width, bottom = size.height)
    }
}

/**
 * Companion to [dpadFocusRing]: shrinks the clipped content inward by [gap] while focused, so the
 * border drawn by [dpadFocusRing] (on the same, unclipped [interactionSource]) has visible
 * breathing room instead of sitting flush against the poster/content edge. Place this *between*
 * [dpadFocusRing] and `Modifier.clip(...)` in the chain - it only affects layout for whatever
 * comes after it (the clip + content), not the outer box's own reported size, so it never causes
 * sibling reflow in a row/grid.
 */
@Composable
fun Modifier.dpadFocusGapPadding(
    interactionSource: InteractionSource,
    gap: Dp = DpadFocusGap,
): Modifier {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val animatedGap by animateDpAsState(
        targetValue = if (isFocused) gap else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "dpad_focus_gap",
    )
    return this.padding(animatedGap)
}

/**
 * Marks a container (row/grid/column of focusable items) as a focus group and translates
 * arrow-key presses into [FocusManager.moveFocus] calls, using Compose's built-in spatial focus
 * search (including its beyond-bounds support for lazy lists) instead of a hand-rolled index
 * system. Key events bubble from the currently-focused leaf upward (`onKeyEvent`, not
 * `onPreviewKeyEvent`), so a focused text field, slider, or dropdown that already consumes
 * arrow keys for its own purpose (cursor movement, value change, etc.) is never intercepted here -
 * this modifier simply never sees those key events.
 */
@Composable
fun Modifier.dpadNavigationContainer(
    enabled: Boolean = true,
    handleUpDown: Boolean = true,
    handleLeftRight: Boolean = true,
): Modifier {
    if (!enabled) return this
    val focusManager = LocalFocusManager.current
    return this
        .focusGroup()
        .onKeyEvent { event -> handleDpadKeyEvent(event, focusManager, handleUpDown, handleLeftRight) }
}

internal fun handleDpadKeyEvent(
    event: KeyEvent,
    focusManager: FocusManager,
    handleUpDown: Boolean = true,
    handleLeftRight: Boolean = true,
): Boolean {
    if (event.type != KeyEventType.KeyDown) return false
    val direction = when (event.key) {
        Key.DirectionUp -> if (handleUpDown) FocusDirection.Up else return false
        Key.DirectionDown -> if (handleUpDown) FocusDirection.Down else return false
        Key.DirectionLeft -> if (handleLeftRight) FocusDirection.Left else return false
        Key.DirectionRight -> if (handleLeftRight) FocusDirection.Right else return false
        else -> return false
    }
    return focusManager.moveFocus(direction)
}

/** Convenience alias: arrow-key navigation on a focus group, in one call. */
@Composable
fun Modifier.dpadFocusable(
    handleUpDown: Boolean = true,
    handleLeftRight: Boolean = true,
): Modifier = dpadNavigationContainer(handleUpDown = handleUpDown, handleLeftRight = handleLeftRight)

/**
 * Holds and lazily creates [FocusRequester]s keyed by a stable item key (poster id, settings
 * category id, etc). Deliberately NOT a global index - each screen/section remembers its own
 * instance, scoped to that screen's composition, so it is discarded when the screen leaves
 * composition (e.g. on navigating to Details and back, restoring is handled by
 * [rememberLastFocusedKey] instead).
 */
@Composable
fun <T : Any> rememberFocusRequesterMap(): FocusRequesterMap<T> = remember { FocusRequesterMap() }

class FocusRequesterMap<T : Any> {
    private val requesters = mutableMapOf<T, FocusRequester>()

    fun forKey(key: T): FocusRequester = requesters.getOrPut(key) { FocusRequester() }

    fun requestFocus(key: T): Boolean {
        val requester = requesters[key] ?: return false
        return try {
            requester.requestFocus()
            true
        } catch (_: IllegalStateException) {
            // Not yet attached to the composition (e.g. item scrolled out of view / not composed).
            false
        }
    }
}

/** Small holder used to restore focus to the last-focused item key when returning to a screen. */
class LastFocusedKeyHolder<T : Any> {
    var key: T? = null
}

@Composable
fun <T : Any> rememberLastFocusedKeyHolder(): LastFocusedKeyHolder<T> = remember { LastFocusedKeyHolder() }

/** Tracks focus-changed events into a [LastFocusedKeyHolder], for use with [Modifier.onFocusChanged]. */
fun <T : Any> LastFocusedKeyHolder<T>.trackFocus(key: T): Modifier = Modifier.onFocusChanged { state: FocusState ->
    if (state.isFocused) this.key = key
}

/** No-op placeholder shape used where callers don't want a focus ring drawn (e.g. plain text). */
val NoFocusRingShape: Shape = RectangleShape

/**
 * Extra top/bottom (and, for non-scrolling grid rows, left/right) space reserved around
 * focus-navigable rows so the focus scale bump has somewhere to render into.
 *
 * Compose's `LazyRow`/`LazyColumn` clip their content to their own *measured* viewport, which is
 * sized from the *unscaled* (pre-`graphicsLayer`) size of their children - a `graphicsLayer` scale
 * is a paint-time-only transform that doesn't grow the reported layout size. So a focused item
 * that pops up by [DpadFocusScale] renders a few dp larger than its measured slot, and without
 * headroom, that overflow gets clipped by the list's own viewport, cropping the rounded corners
 * of the focus ring (and, worse, of the poster itself). Reserving this margin at the row/list
 * level - not by padding every individual card - keeps posters exactly their configured size and
 * doesn't require every screen to duplicate this math.
 */
val FocusOverflowReserve = 10.dp

/** [PaddingValues] variant of [FocusOverflowReserve], merged additively with an existing value. */
@Composable
fun PaddingValues.withFocusOverflowReserve(): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(layoutDirection),
        end = calculateEndPadding(layoutDirection),
        top = calculateTopPadding() + FocusOverflowReserve,
        bottom = calculateBottomPadding() + FocusOverflowReserve,
    )
}

/**
 * Direction of the arrow key that woke up keyboard/D-pad navigation on a screen that has no
 * Compose focus yet. Deliberately separate from [FocusDirection] since this is used at the
 * "nothing is focused yet" boundary (see [DpadNavActivation]), before any Compose focus node
 * exists to express a [FocusDirection] against.
 */
enum class NavArrowKeyDirection {
    Up,
    Down,
    Left,
    Right,
    ;

    companion object {
        fun fromKey(key: Key): NavArrowKeyDirection? = when (key) {
            Key.DirectionUp -> Up
            Key.DirectionDown -> Down
            Key.DirectionLeft -> Left
            Key.DirectionRight -> Right
            else -> null
        }
    }
}

/**
 * Bridges the very first arrow-key press on a screen (before anything in that screen has real
 * Compose focus) to whichever screen is currently active/visible.
 *
 * Compose only dispatches key events through its focus tree once *something* has focus (see the
 * comment on `installDesktopNavigationShortcuts`), and this app deliberately never grabs focus on
 * screen mount - keyboard/D-pad navigation should stay invisible until the user actually presses
 * an arrow key. So the very first press has nowhere to go via Compose's own dispatch. On desktop,
 * that first press is caught by the existing AWT-level global shortcut dispatcher (same mechanism
 * as F11/tab-switch shortcuts) and routed here; this object then hands it to whichever screen is
 * currently the active tab, which requests focus onto its own screen-appropriate initial target
 * (see each screen's `DisposableEffect` registration). Every press *after* that first one is
 * handled entirely by normal Compose focus dispatch (`dpadNavigationContainer`), since something
 * now has real focus.
 *
 * Each screen registers itself under its own stable tab key and only while it's the current
 * target; this is a small per-tab slot, not a global index of items.
 */
object DpadNavActivation {
    private val activators = mutableMapOf<String, (NavArrowKeyDirection) -> Boolean>()

    /** Registers [activator] under [tabKey]. Returns a disposer to call on leaving composition. */
    fun register(tabKey: String, activator: (NavArrowKeyDirection) -> Boolean): () -> Unit {
        activators[tabKey] = activator
        return { if (activators[tabKey] === activator) activators.remove(tabKey) }
    }

    /**
     * Called (from the platform shortcut layer) with the currently active tab's key and the
     * pressed direction. Returns true if a screen activated its navigation for this press (in
     * which case the platform layer should consume the key event so it isn't also interpreted as
     * a "move" immediately after activation); false if there was nothing to activate (already
     * active, or no screen registered), in which case the platform layer should let the key event
     * continue through to Compose's normal focus-based dispatch.
     */
    fun tryActivate(tabKey: String, direction: NavArrowKeyDirection): Boolean =
        activators[tabKey]?.invoke(direction) ?: false
}

/**
 * Convenience for a screen to register/unregister its [DpadNavActivation] slot for the lifetime
 * of its composition.
 */
@Composable
fun RegisterDpadNavActivation(tabKey: String, activator: (NavArrowKeyDirection) -> Boolean) {
    DisposableEffect(tabKey) {
        val unregister = DpadNavActivation.register(tabKey, activator)
        onDispose { unregister() }
    }
}
