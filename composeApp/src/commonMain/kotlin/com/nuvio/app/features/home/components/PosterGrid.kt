package com.nuvio.app.features.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import com.nuvio.app.core.ui.focus.dpadFocusGapPadding
import com.nuvio.app.core.ui.focus.dpadFocusRing
import com.nuvio.app.core.ui.focus.DpadFocusGap
import com.nuvio.app.core.ui.focus.FocusOverflowReserve
import com.nuvio.app.core.ui.focus.LocalStickyHeaderInsetPx
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntSize
import coil3.compose.AsyncImage
import com.nuvio.app.core.format.formatReleaseDateForDisplay
import com.nuvio.app.core.ui.NuvioCardDepthSurface
import com.nuvio.app.core.ui.NuvioPosterWatchedOverlay
import com.nuvio.app.core.ui.nuvioCardDepth
import com.nuvio.app.core.ui.posterCardClickable
import com.nuvio.app.core.ui.rememberPosterCardStyleUiState
import com.nuvio.app.features.home.MetaPreview
import com.nuvio.app.features.home.PosterShape
import com.nuvio.app.features.watching.application.WatchingState
import kotlinx.coroutines.launch

internal fun posterGridColumnCountForWidth(screenWidth: Dp): Int =
    when {
        screenWidth >= 1400.dp -> 7
        screenWidth >= 1200.dp -> 6
        screenWidth >= 1000.dp -> 5
        screenWidth >= 840.dp -> 4
        else -> 3
    }

@Composable
internal fun PosterGridRow(
    items: List<MetaPreview>,
    columns: Int,
    modifier: Modifier = Modifier,
    watchedKeys: Set<String> = emptySet(),
    fullyWatchedSeriesKeys: Set<String> = emptySet(),
    onPosterClick: ((MetaPreview) -> Unit)? = null,
    onPosterLongClick: ((MetaPreview) -> Unit)? = null,
    focusRequesterFor: ((MetaPreview) -> FocusRequester?)? = null,
    onPosterFocusChanged: ((MetaPreview, Boolean) -> Unit)? = null,
) {
    val posterCardStyle = rememberPosterCardStyleUiState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = FocusOverflowReserve / 2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        items.forEach { item ->
            PosterGridTile(
                item = item,
                cornerRadiusDp = posterCardStyle.cornerRadiusDp,
                hideLabels = posterCardStyle.hideLabelsEnabled,
                modifier = Modifier.weight(1f),
                isWatched = WatchingState.isPosterWatched(
                    watchedKeys = watchedKeys,
                    item = item,
                    fullyWatchedSeriesKeys = fullyWatchedSeriesKeys,
                ),
                onClick = onPosterClick?.let { { it(item) } },
                onLongClick = onPosterLongClick?.let { { it(item) } },
                focusRequester = focusRequesterFor?.invoke(item),
                onFocusChanged = onPosterFocusChanged?.let { callback -> { focused: Boolean -> callback(item, focused) } },
            )
        }
        repeat(columns - items.size) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
internal fun PosterGridSkeletonRow(
    columns: Int,
    modifier: Modifier = Modifier,
) {
    val posterCardStyle = rememberPosterCardStyleUiState()

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        repeat(columns) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(0.68f)
                    .clip(RoundedCornerShape(posterCardStyle.cornerRadiusDp.dp))
                    .background(MaterialTheme.colorScheme.surface),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PosterGridTile(
    item: MetaPreview,
    cornerRadiusDp: Int,
    hideLabels: Boolean,
    modifier: Modifier = Modifier,
    isWatched: Boolean = false,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    focusRequester: FocusRequester? = null,
    onFocusChanged: ((Boolean) -> Unit)? = null,
) {
    HomePosterHoverPreview(
        item = item,
        isWatched = isWatched,
        onClick = onClick,
        onLongClick = onLongClick,
        modifier = modifier,
    ) {
        val posterInteractionSource = remember { MutableInteractionSource() }
        val posterBringIntoViewRequester = remember { BringIntoViewRequester() }
        val posterBringIntoViewScope = rememberCoroutineScope()
        var posterCardSizePx by remember { mutableStateOf(IntSize.Zero) }
        val stickyHeaderInsetPx = LocalStickyHeaderInsetPx.current
        // See NuvioPosterCard for why this is needed: the focusable target is the image Box
        // only, so the default scroll-into-view doesn't know about the label below it, and the
        // sticky-header-height padding keeps it from ending up scrolled to right underneath a
        // persistent header (e.g. Search's search box, Library's title bar) when navigating up.
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
                .fillMaxWidth()
                .then(it)
                .onSizeChanged { newSize -> posterCardSizePx = newSize }
                .bringIntoViewRequester(posterBringIntoViewRequester),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(item.posterShape.posterGridAspectRatio())
                    .dpadFocusRing(
                        interactionSource = posterInteractionSource,
                        cornerRadius = cornerRadiusDp.dp,
                        gap = DpadFocusGap,
                    )
                    .dpadFocusGapPadding(interactionSource = posterInteractionSource)
                    .clip(RoundedCornerShape(cornerRadiusDp.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .nuvioCardDepth(
                        shape = RoundedCornerShape(cornerRadiusDp.dp),
                        surface = NuvioCardDepthSurface.Posters,
                    )
                    .posterCardClickable(
                        onClick = onClick,
                        onLongClick = onLongClick,
                        zoomImageUrl = item.poster,
                        zoomCornerRadius = cornerRadiusDp.dp,
                        focusRequester = focusRequester,
                        onFocusChanged = combinedOnFocusChanged,
                        interactionSource = posterInteractionSource,
                        drawFocusRing = false,
                    ),
            ) {
                if (item.poster != null) {
                    AsyncImage(
                        model = item.poster,
                        contentDescription = item.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
                NuvioPosterWatchedOverlay(isWatched = isWatched)
            }
            if (!hideLabels) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                val detail = item.releaseInfo?.let { formatReleaseDateForDisplay(it) }
                if (detail != null) {
                    Text(
                        text = detail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

private fun PosterShape.posterGridAspectRatio(): Float =
    when (this) {
        PosterShape.Poster -> 0.68f
        PosterShape.Square -> 1f
        PosterShape.Landscape -> 1.78f
    }
