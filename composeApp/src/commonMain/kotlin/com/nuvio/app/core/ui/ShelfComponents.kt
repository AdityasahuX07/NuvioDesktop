package com.nuvio.app.core.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import com.nuvio.app.core.ui.focus.dpadFocusGapPadding
import com.nuvio.app.core.ui.focus.dpadFocusRing
import com.nuvio.app.core.ui.focus.dpadNavigationContainer
import com.nuvio.app.core.ui.focus.DpadFocusGap
import com.nuvio.app.core.ui.focus.LocalStickyHeaderInsetPx
import com.nuvio.app.core.ui.focus.withFocusOverflowReserve
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.nuvio.app.isDesktop
import nuvio.composeapp.generated.resources.Res
import nuvio.composeapp.generated.resources.home_view_all
import nuvio.composeapp.generated.resources.poster_logo_content_description
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class NuvioPosterShape {
    Poster,
    Square,
    Landscape,
}

enum class NuvioViewAllPillSize {
    Default,
    Compact,
}

@Composable
fun <T> NuvioShelfSection(
    title: String,
    entries: List<T>,
    modifier: Modifier = Modifier,
    rowModifier: Modifier = Modifier,
    headerHorizontalPadding: Dp = 0.dp,
    rowContentPadding: PaddingValues = PaddingValues(0.dp),
    itemSpacing: Dp = 10.dp,
    onViewAllClick: (() -> Unit)? = null,
    onTitleClick: (() -> Unit)? = null,
    viewAllPillSize: NuvioViewAllPillSize = NuvioViewAllPillSize.Default,
    key: ((T) -> Any)? = null,
    animatePlacement: Boolean = false,
    state: LazyListState = rememberLazyListState(),
    itemContent: @Composable (T) -> Unit,
) {
    val tokens = MaterialTheme.nuvio
    val duplicateSafeEntries = remember(entries, key) {
        key?.let { entries.withDuplicateSafeLazyKeys(it) }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(tokens.spacing.controlGap + NuvioTokens.Space.s2),
    ) {
        if (title.isNotBlank()) {
            NuvioShelfSectionHeader(
                title = title,
                modifier = Modifier.padding(horizontal = headerHorizontalPadding),
                onViewAllClick = onViewAllClick,
                onTitleClick = onTitleClick,
                viewAllPillSize = viewAllPillSize,
            )
        }
        // When the row has been horizontally scrolled and the user navigates back to the first
        // item, snap the row back to its true start (index 0) instead of leaving it mid-scroll -
        // otherwise the first item's focus scale/border, still offset from the row's actual
        // start, can end up partly misaligned with the section title above it (or, at the screen
        // edge, clipped). This only ever targets the very first item.
        //
        // The short delay before scrolling matters: Compose's own default "bring focused item
        // into view" behavior (bundled into `focusable()`) also fires on this same focus change,
        // targeting just enough scroll to reveal the item - not necessarily index 0. Both
        // requests land on the same LazyListState, and whichever is issued *last* wins; firing
        // ours immediately risks losing that race and ending up scrolled to wherever Compose's
        // own version stopped. Waiting lets that default settle first, so our explicit
        // scroll-to-start is the one that actually sticks. Driving it off a LaunchedEffect
        // (rather than launching directly from the callback) also means a rapid focus-in/
        // focus-out doesn't stack up multiple competing scroll jobs - the pending delay is
        // simply cancelled if focus leaves before it fires.
        var firstItemHasFocus by remember { mutableStateOf(false) }
        LaunchedEffect(firstItemHasFocus) {
            if (firstItemHasFocus) {
                delay(120L)
                runCatching { state.scrollToItem(0) }
                runCatching { state.animateScrollToItem(0) }
            }
        }
        val firstItemFocusModifier = Modifier.onFocusChanged { focusState ->
            firstItemHasFocus = focusState.hasFocus
        }
        LazyRow(
            state = state,
            modifier = rowModifier
                .nuvioDesktopDragScroll(state)
                .dpadNavigationContainer(handleUpDown = false),
            contentPadding = rowContentPadding.withFocusOverflowReserve(),
            horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        ) {
            if (duplicateSafeEntries != null) {
                itemsIndexed(
                    items = duplicateSafeEntries,
                    key = { _, entry -> entry.lazyKey },
                    contentType = { _, _ -> "poster" },
                ) { index, keyedEntry ->
                    val content: @Composable () -> Unit = {
                        if (animatePlacement) {
                            Box(modifier = Modifier.animateItem()) { itemContent(keyedEntry.value) }
                        } else {
                            itemContent(keyedEntry.value)
                        }
                    }
                    if (index == 0) {
                        Box(modifier = firstItemFocusModifier) { content() }
                    } else {
                        content()
                    }
                }
            } else {
                itemsIndexed(
                    items = entries,
                    contentType = { _, _ -> "poster" },
                ) { index, entry ->
                    val content: @Composable () -> Unit = {
                        if (animatePlacement) {
                            Box(modifier = Modifier.animateItem()) { itemContent(entry) }
                        } else {
                            itemContent(entry)
                        }
                    }
                    if (index == 0) {
                        Box(modifier = firstItemFocusModifier) { content() }
                    } else {
                        content()
                    }
                }
            }
        }
    }
}

internal fun Modifier.nuvioDesktopDragScroll(
    state: LazyListState,
): Modifier {
    if (!isDesktop) return this

    return pointerInput(state) {
        awaitEachGesture {
            val down = awaitFirstDown(pass = PointerEventPass.Initial)
            var totalDx = 0f
            var totalDy = 0f
            var dragging = false

            while (true) {
                val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                if (!change.pressed) break

                val delta = change.position - change.previousPosition
                totalDx += delta.x
                totalDy += delta.y

                if (!dragging) {
                    val horizontalDrag =
                        abs(totalDx) > viewConfiguration.touchSlop && abs(totalDx) > abs(totalDy)
                    val verticalDrag =
                        abs(totalDy) > viewConfiguration.touchSlop && abs(totalDy) > abs(totalDx)

                    when {
                        verticalDrag -> break
                        horizontalDrag -> dragging = true
                        else -> continue
                    }
                }

                state.dispatchRawDelta(-delta.x)
                change.consume()
            }
        }
    }
}

internal fun Modifier.nuvioDesktopDragScroll(
    state: ScrollState,
): Modifier {
    if (!isDesktop) return this

    return pointerInput(state) {
        awaitEachGesture {
            val down = awaitFirstDown(pass = PointerEventPass.Initial)
            var totalDx = 0f
            var totalDy = 0f
            var dragging = false

            while (true) {
                val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                if (!change.pressed) break

                val delta = change.position - change.previousPosition
                totalDx += delta.x
                totalDy += delta.y

                if (!dragging) {
                    val horizontalDrag =
                        abs(totalDx) > viewConfiguration.touchSlop && abs(totalDx) > abs(totalDy)
                    val verticalDrag =
                        abs(totalDy) > viewConfiguration.touchSlop && abs(totalDy) > abs(totalDx)

                    when {
                        verticalDrag -> break
                        horizontalDrag -> dragging = true
                        else -> continue
                    }
                }

                state.dispatchRawDelta(-delta.x)
                change.consume()
            }
        }
    }
}

@Composable
fun NuvioPosterCard(
    title: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    basePosterWidthDp: Int? = null,
    shape: NuvioPosterShape = NuvioPosterShape.Poster,
    detailLine: String? = null,
    showTitleBelow: Boolean = true,
    bottomLeftLogoUrl: String? = null,
    bottomLeftText: String? = null,
    isWatched: Boolean = false,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    focusRequester: FocusRequester? = null,
    onFocusChanged: ((Boolean) -> Unit)? = null,
) {
    val posterCardStyle = rememberPosterCardStyleUiState()
    val tokens = MaterialTheme.nuvio
    val effectiveBasePosterWidthDp = basePosterWidthDp ?: posterCardStyle.widthDp
    val cardWidth = shape.cardWidth(basePosterWidthDp = effectiveBasePosterWidthDp)
    val cardShape = RoundedCornerShape(posterCardStyle.cornerRadiusDp.dp)
    val catalogLogoOverlaySize = catalogLogoOverlaySize(
        basePosterWidthDp = effectiveBasePosterWidthDp,
        shape = shape,
    )
    val shouldShowTitleBelow = showTitleBelow && !posterCardStyle.hideLabelsEnabled
    val posterInteractionSource = remember { MutableInteractionSource() }
    val posterBringIntoViewRequester = remember { BringIntoViewRequester() }
    val posterBringIntoViewScope = rememberCoroutineScope()
    var posterCardSizePx by remember { mutableStateOf(IntSize.Zero) }
    val stickyHeaderInsetPx = LocalStickyHeaderInsetPx.current
    // The focusable/clickable target below is the image Box only, so Compose's automatic
    // scroll-focused-item-into-view only guarantees the image is visible - it has no idea the
    // title/year label below is part of the same card. Bringing this whole Column (image + label)
    // into view explicitly whenever it gains focus keeps the label from getting cropped by the
    // viewport edge when navigating to the last visible row. Padding the requested rect upward by
    // the screen's sticky header height (if any - see LocalStickyHeaderInsetPx) additionally
    // keeps the poster from ending up scrolled to directly underneath that overlay when
    // navigating up into it.
    val combinedOnFocusChanged: (Boolean) -> Unit = { focused ->
        onFocusChanged?.invoke(focused)
        if (focused) {
            posterBringIntoViewScope.launch {
                runCatching {
                    posterBringIntoViewRequester.bringIntoView(
                        Rect(
                            left = 0f,
                            top = -stickyHeaderInsetPx.toFloat(),
                            right = posterCardSizePx.width.toFloat(),
                            bottom = posterCardSizePx.height.toFloat(),
                        ),
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .desktopPosterHoverScale()
            .then(modifier)
            .width(cardWidth)
            .onSizeChanged { posterCardSizePx = it }
            .bringIntoViewRequester(posterBringIntoViewRequester),
        verticalArrangement = Arrangement.spacedBy(NuvioTokens.Space.s6),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(shape.aspectRatio)
                .dpadFocusRing(
                    interactionSource = posterInteractionSource,
                    cornerRadius = posterCardStyle.cornerRadiusDp.dp,
                    gap = DpadFocusGap,
                )
                .dpadFocusGapPadding(interactionSource = posterInteractionSource)
                .clip(cardShape)
                .background(tokens.colors.surface)
                .nuvioCardDepth(
                    shape = cardShape,
                    surface = NuvioCardDepthSurface.Posters,
                )
                .posterCardClickable(
                    onClick = onClick,
                    onLongClick = onLongClick,
                    zoomImageUrl = imageUrl,
                    zoomCornerRadius = posterCardStyle.cornerRadiusDp.dp,
                    hoverScaleEnabled = false,
                    focusRequester = focusRequester,
                    onFocusChanged = combinedOnFocusChanged,
                    interactionSource = posterInteractionSource,
                    drawFocusRing = false,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (imageUrl != null) {
                NuvioAsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Text(
                    text = title,
                    modifier = Modifier.padding(horizontal = NuvioTokens.Space.s14),
                    style = MaterialTheme.typography.titleMedium,
                    color = tokens.colors.textMuted,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (!bottomLeftLogoUrl.isNullOrBlank() || !bottomLeftText.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = NuvioTokens.Space.s10, vertical = NuvioTokens.Space.s10),
                ) {
                    if (!bottomLeftLogoUrl.isNullOrBlank()) {
                        NuvioAsyncImage(
                            model = bottomLeftLogoUrl,
                            contentDescription = stringResource(Res.string.poster_logo_content_description, title),
                            modifier = Modifier
                                .width(catalogLogoOverlaySize.width)
                                .height(catalogLogoOverlaySize.height),
                            contentScale = ContentScale.Fit,
                        )
                    } else {
                        Text(
                            text = bottomLeftText.orEmpty(),
                            style = MaterialTheme.typography.labelMedium,
                            color = tokens.colors.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = catalogLogoOverlaySize.textMaxWidth),
                        )
                    }
                }
            }

            NuvioPosterWatchedOverlay(isWatched = isWatched)
        }
        if (shouldShowTitleBelow) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = tokens.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (!detailLine.isNullOrBlank()) {
                Text(
                    text = detailLine,
                    style = MaterialTheme.typography.labelSmall,
                    color = tokens.colors.textMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            } else {
                Box(modifier = Modifier.height(NuvioTokens.Space.none))
            }
        } else {
            Box(modifier = Modifier.height(NuvioTokens.Space.none))
        }
    }
}

@Composable
private fun NuvioShelfSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    onViewAllClick: (() -> Unit)? = null,
    onTitleClick: (() -> Unit)? = null,
    viewAllPillSize: NuvioViewAllPillSize = NuvioViewAllPillSize.Default,
) {
    val tokens = MaterialTheme.nuvio
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(tokens.spacing.controlGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .then(if (onTitleClick != null) Modifier.focusProperties { canFocus = false }.clickable(onClick = onTitleClick) else Modifier),
                style = MaterialTheme.typography.titleLarge,
                color = tokens.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            val viewAllPlaceholderModifier = if (onViewAllClick == null) {
                Modifier
                    .alpha(0f)
                    .clearAndSetSemantics { }
            } else {
                Modifier
            }
            NuvioViewAllPill(
                onClick = onViewAllClick,
                size = viewAllPillSize,
                modifier = viewAllPlaceholderModifier,
            )
        }
    }
}

@Composable
private fun NuvioViewAllPill(
    onClick: (() -> Unit)?,
    size: NuvioViewAllPillSize,
    modifier: Modifier = Modifier,
) {
    val tokens = MaterialTheme.nuvio
    val actionSize = if (size == NuvioViewAllPillSize.Compact) NuvioTokens.Space.s32 else NuvioTokens.Space.s40
    val iconSize = if (size == NuvioViewAllPillSize.Compact) NuvioTokens.Icon.sm else tokens.icons.md
    val viewAllText = stringResource(Res.string.home_view_all)

    Box(
        modifier = modifier
            .size(actionSize)
            .background(
                color = tokens.colors.surface,
                shape = RoundedCornerShape(NuvioTokens.Radius.xl),
            )
            .then(if (onClick != null) Modifier.focusProperties { canFocus = false }.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = viewAllText,
            tint = tokens.colors.textMuted,
            modifier = Modifier.size(iconSize),
        )
    }
}

internal const val NuvioDesktopCatalogShelfPosterScale = 1.4f
private const val DesktopPosterHoverScale = 1.045f

internal fun desktopCatalogShelfPosterBaseWidthDp(
    basePosterWidthDp: Int,
): Int =
    if (isDesktop) {
        (basePosterWidthDp * NuvioDesktopCatalogShelfPosterScale).roundToInt()
    } else {
        basePosterWidthDp
    }

internal fun Modifier.nuvioShelfHoverOverdraw(inset: Dp): Modifier {
    if (inset == 0.dp) return this

    // Expand the measured viewport, then place it negatively so edge items keep
    // their visual alignment while desktop hover scale can draw into the gutter.
    return layout { measurable, constraints ->
        val insetPx = inset.roundToPx()
        val horizontalInset = insetPx * 2
        val verticalInset = insetPx * 2
        val expandedMaxWidth = if (constraints.hasBoundedWidth) {
            constraints.maxWidth + horizontalInset
        } else {
            constraints.maxWidth
        }
        val expandedMinWidth = if (constraints.hasBoundedWidth) {
            (constraints.minWidth + horizontalInset).coerceAtMost(expandedMaxWidth)
        } else {
            constraints.minWidth
        }
        val expandedMaxHeight = if (constraints.hasBoundedHeight) {
            constraints.maxHeight + verticalInset
        } else {
            constraints.maxHeight
        }
        val expandedConstraints = constraints.copy(
            minWidth = expandedMinWidth,
            maxWidth = expandedMaxWidth,
            minHeight = 0,
            maxHeight = expandedMaxHeight,
        )
        val placeable = measurable.measure(expandedConstraints)
        val width = (placeable.width - horizontalInset)
            .coerceIn(constraints.minWidth, constraints.maxWidth)
        val height = (placeable.height - verticalInset)
            .coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(width, height) {
            placeable.placeRelative(-insetPx, -insetPx)
        }
    }
}

private val NuvioPosterShape.aspectRatio: Float
    get() = when (this) {
        NuvioPosterShape.Poster -> 0.675f
        NuvioPosterShape.Square -> 1f
        NuvioPosterShape.Landscape -> PosterLandscapeAspectRatio
    }

private data class CatalogLogoOverlaySize(
    val width: Dp,
    val height: Dp,
    val textMaxWidth: Dp,
)

private fun catalogLogoOverlaySize(
    basePosterWidthDp: Int,
    shape: NuvioPosterShape,
): CatalogLogoOverlaySize =
    if (shape == NuvioPosterShape.Landscape) {
        when {
            basePosterWidthDp <= 108 -> CatalogLogoOverlaySize(width = 92.dp, height = 24.dp, textMaxWidth = 120.dp)
            basePosterWidthDp <= 120 -> CatalogLogoOverlaySize(width = 104.dp, height = 28.dp, textMaxWidth = 132.dp)
            basePosterWidthDp <= 132 -> CatalogLogoOverlaySize(width = 116.dp, height = 30.dp, textMaxWidth = 144.dp)
            else -> CatalogLogoOverlaySize(width = 128.dp, height = 34.dp, textMaxWidth = 156.dp)
        }
    } else {
        when {
            basePosterWidthDp <= 108 -> CatalogLogoOverlaySize(width = 72.dp, height = 18.dp, textMaxWidth = 92.dp)
            basePosterWidthDp <= 120 -> CatalogLogoOverlaySize(width = 80.dp, height = 20.dp, textMaxWidth = 104.dp)
            basePosterWidthDp <= 132 -> CatalogLogoOverlaySize(width = 88.dp, height = 22.dp, textMaxWidth = 112.dp)
            else -> CatalogLogoOverlaySize(width = 96.dp, height = 24.dp, textMaxWidth = 124.dp)
        }
    }

private fun NuvioPosterShape.cardWidth(basePosterWidthDp: Int): Dp =
    when (this) {
        NuvioPosterShape.Poster -> basePosterWidthDp.dp
        NuvioPosterShape.Square -> basePosterWidthDp.dp
        NuvioPosterShape.Landscape -> landscapePosterWidth(basePosterWidthDp)
    }

@Composable
internal fun Modifier.desktopPosterHoverScale(
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
): Modifier {
    if (!enabled || !isDesktop) return this

    val hovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (hovered) DesktopPosterHoverScale else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "desktop_poster_hover_scale",
    )

    val isScaling = hovered || scale != 1f

    return this
        .then(
            if (isScaling) {
                Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .zIndex(1f)
            } else {
                Modifier
            },
        )
        .hoverable(interactionSource)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Modifier.posterCardClickable(
    onClick: (() -> Unit)?,
    onLongClick: (() -> Unit)?,
    zoomImageUrl: String? = null,
    zoomCornerRadius: Dp = NuvioTokens.Radius.poster,
    hoverScaleEnabled: Boolean = true,
    focusRequester: FocusRequester? = null,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    drawFocusRing: Boolean = true,
): Modifier {
    if (onClick == null && onLongClick == null) return this
    val bounds = remember { mutableStateOf<Rect?>(null) }
    val resolvedInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val handleLongClick = onLongClick?.let { longClick ->
        {
            bounds.value?.takeIf { zoomImageUrl != null }?.let { cardBounds ->
                PosterZoomAnchorHolder.stash(
                    PosterZoomAnchor(
                        boundsInRoot = cardBounds,
                        imageUrl = zoomImageUrl,
                        cornerRadius = zoomCornerRadius,
                    ),
                )
            }
            longClick()
        }
    }
    return this
        .onGloballyPositioned { coordinates -> bounds.value = coordinates.unclippedBoundsInRoot() }
        .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
        .then(
            if (onFocusChanged != null) {
                Modifier.onFocusChanged { onFocusChanged(it.isFocused) }
            } else {
                Modifier
            },
        )
        .desktopPosterHoverScale(
            enabled = hoverScaleEnabled,
            interactionSource = resolvedInteractionSource,
        )
        .then(
            if (drawFocusRing) {
                Modifier.dpadFocusRing(
                    interactionSource = resolvedInteractionSource,
                    cornerRadius = zoomCornerRadius,
                )
            } else {
                Modifier
            },
        )
        .combinedClickable(
            interactionSource = resolvedInteractionSource,
            indication = null,
            onClick = { onClick?.invoke() },
            onLongClick = handleLongClick,
        )
        .secondaryClick(handleLongClick)
}

private fun androidx.compose.ui.layout.LayoutCoordinates.unclippedBoundsInRoot(): Rect {
    val position = positionInRoot()
    return Rect(
        left = position.x,
        top = position.y,
        right = position.x + size.width,
        bottom = position.y + size.height,
    )
}
