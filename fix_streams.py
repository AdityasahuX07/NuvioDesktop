import re

with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/streams/StreamsTabletLayout.kt', 'r', encoding='utf-8') as f:
    c = f.read()

c = c.replace('''    onStreamSecondaryClick: (StreamItem, Offset) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,''', '''    onStreamSecondaryClick: (StreamItem, Offset) -> Unit,
    onRefresh: () -> Unit,
    listState: androidx.compose.foundation.lazy.LazyListState,
    streamSections: List<StreamSectionRenderModel>,
    modifier: Modifier = Modifier,''')

c = c.replace('''        LegacyTabletStreamsLayout(
            isEpisode = isEpisode,
            title = title,
            logo = logo,
            heroArtwork = background ?: poster,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            episodeTitle = episodeTitle,
            blurEpisodeThumbnail = false,
            uiState = uiState,
            debridEnabled = debridEnabled,
            appendInstantServiceToDefaultName = appendInstantServiceToDefaultName,
            resumePositionMs = resumePositionMs,
            resumeProgressFraction = resumeProgressFraction,
            onStreamSelected = onStreamSelected,
            onStreamLongPress = onStreamLongPress,
            onStreamSecondaryClick = onStreamSecondaryClick,
            onRefresh = onRefresh,
            modifier = modifier,
        )''', '''        LegacyTabletStreamsLayout(
            isEpisode = isEpisode,
            title = title,
            logo = logo,
            heroArtwork = background ?: poster,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            episodeTitle = episodeTitle,
            blurEpisodeThumbnail = false,
            uiState = uiState,
            debridEnabled = debridEnabled,
            appendInstantServiceToDefaultName = appendInstantServiceToDefaultName,
            resumePositionMs = resumePositionMs,
            resumeProgressFraction = resumeProgressFraction,
            onStreamSelected = onStreamSelected,
            onStreamLongPress = onStreamLongPress,
            onStreamSecondaryClick = onStreamSecondaryClick,
            onRefresh = onRefresh,
            listState = listState,
            streamSections = streamSections,
            modifier = modifier,
        )''')

with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/streams/StreamsTabletLayout.kt', 'w', encoding='utf-8') as f:
    f.write(c)
