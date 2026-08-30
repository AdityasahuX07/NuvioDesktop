import os, re

def replace_regex(filepath, pattern, replacement):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    content = re.sub(pattern, replacement, content, flags=re.DOTALL)
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

# 1. Components.kt
replace_regex('composeApp/src/commonMain/kotlin/com/nuvio/app/core/ui/Components.kt',
r'<<<<<<< HEAD.*?\.background\(containerColor\).*?=======\s*\.background\(effectiveContainerColor\).*?>>>>>>> origin/Dev',
'''            .background(effectiveContainerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick,
            ),''')

# 2. FolderDetailScreen.kt
replace_regex('composeApp/src/commonMain/kotlin/com/nuvio/app/features/collection/FolderDetailScreen.kt',
r'<<<<<<< HEAD.*?FolderViewMode\.ROWS -> RowsContent\(.*?homeFocusCoordinator = homeFocusCoordinator,.*?FolderViewMode\.FOLLOW_LAYOUT -> RowsContent\(.*?=======\s*FolderViewMode\.ROWS,\s*FolderViewMode\.FOLLOW_LAYOUT,\s*-> RowsContent\(.*?>>>>>>> origin/Dev',
'''            FolderViewMode.ROWS,
            FolderViewMode.FOLLOW_LAYOUT,
            -> RowsContent(''')

# 3. DesktopDetailHero.kt
replace_regex('composeApp/src/commonMain/kotlin/com/nuvio/app/features/details/components/DesktopDetailHero.kt',
r'<<<<<<< HEAD(.*?)=======\s*modifier: Modifier = Modifier,\s*>>>>>>> origin/Dev',
r'\1    modifier: Modifier = Modifier,')

# 4. HomeHeroSection.kt
replace_regex('composeApp/src/commonMain/kotlin/com/nuvio/app/features/home/components/HomeHeroSection.kt',
r'<<<<<<< HEAD\s*if \(isFullscreenActionSupported\).*?=======\s*HeroPageIndicatorRow\(.*?>>>>>>> origin/Dev',
'''        if (isFullscreenActionSupported) {
            val homeFocusCoordinatorForFullscreen = LocalHomeFocusCoordinator.current
            FullscreenActionButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = space.s32,
                        end = contentHorizontalPadding,
                    )
                    .then(
                        if (homeFocusCoordinatorForFullscreen != null) {
                            Modifier.focusProperties {
                                down = homeFocusCoordinatorForFullscreen.heroViewDetailsFocusRequester
                            }
                        } else {
                            Modifier
                        }
                    ),
                buttonSize = 48.dp,
                iconSize = 24.dp,
                containerColor = colorScheme.surfaceVariant.copy(alpha = 0.82f),
                contentColor = colorScheme.onSurface,
            )
        }

        HeroPageIndicatorRow(
            itemCount = items.size,
            pagerState = pagerState,
            coroutineScope = coroutineScope,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = contentHorizontalPadding,
                    bottom = space.s40,
                ),
        )''')

# 5. PosterGrid.kt
replace_regex('composeApp/src/commonMain/kotlin/com/nuvio/app/features/home/components/PosterGrid.kt',
r'<<<<<<< HEAD(.*?)=======\s*>>>>>>> origin/Dev',
r'\1')

replace_regex('composeApp/src/commonMain/kotlin/com/nuvio/app/features/home/components/PosterGrid.kt',
r'<<<<<<< HEAD\s*top = 12\.dp,\s*start = 16\.dp,\s*end = 16\.dp,\s*=======\s*start = pageHorizontalPadding,\s*end = pageHorizontalPadding,\s*>>>>>>> origin/Dev',
'''                                top = 12.dp,
                                start = pageHorizontalPadding,
                                end = pageHorizontalPadding,''')

# 6. LibraryScreen.kt
replace_regex('composeApp/src/commonMain/kotlin/com/nuvio/app/features/library/LibraryScreen.kt',
r'<<<<<<< HEAD(.*?)val gridColumns = remember\(maxWidth\).*?=======\s*val posterCardStyle = rememberPosterCardStyleUiState\(\).*?>>>>>>> origin/Dev',
r'''\1
        val posterCardStyle = rememberPosterCardStyleUiState()
        val gridColumns = remember(maxWidth, maxHeight, posterCardStyle.widthDp, isDesktop) {
            if (isDesktop) {
                posterGridColumnCountForViewport(maxWidth, maxHeight, posterCardStyle.widthDp)
            } else {
                posterGridColumnCountForWidth(maxWidth)
            }
        }''')

# 7. SearchScreen.kt
replace_regex('composeApp/src/commonMain/kotlin/com/nuvio/app/features/search/SearchScreen.kt',
r'<<<<<<< HEAD\s*(.*?)\s*=======\s*(.*?)\s*>>>>>>> origin/Dev',
r'\2\n\1')

# 8. StreamCard.kt
replace_regex('composeApp/src/commonMain/kotlin/com/nuvio/app/features/streams/StreamCard.kt',
r'<<<<<<< HEAD\s*import androidx.compose.ui.focus.onFocusChanged(.*?)\s*=======\s*import com.nuvio.app.isDesktop\s*>>>>>>> origin/Dev',
r'''import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import com.nuvio.app.isDesktop''')

replace_regex('composeApp/src/commonMain/kotlin/com/nuvio/app/features/streams/StreamCard.kt',
r'<<<<<<< HEAD(.*?)=======\s*var cardPositionInRoot by remember \{ mutableStateOf\(Offset\.Zero\) \}\s*>>>>>>> origin/Dev',
r'\1\n    var cardPositionInRoot by remember { mutableStateOf(Offset.Zero) }')

replace_regex('composeApp/src/commonMain/kotlin/com/nuvio/app/features/streams/StreamCard.kt',
r'<<<<<<< HEAD(.*?)=======\s*\.then\(\s*if \(isDesktop\).*?>>>>>>> origin/Dev',
r'''            .focusRequester(itemFocusRequester)
            .focusable()
            .onFocusChanged { isNativelyFocused = it.isFocused }
            .then(
                if (isDesktop) {
                    Modifier.onGloballyPositioned { coordinates ->
                        cardPositionInRoot = coordinates.positionInRoot()
                    }
                } else {
                    Modifier
                }
            )''')

# 9. StreamsScreen.kt
replace_regex('composeApp/src/commonMain/kotlin/com/nuvio/app/features/streams/StreamsScreen.kt',
r'<<<<<<< HEAD(.*?)=======\s*(.*?)\s*>>>>>>> origin/Dev',
r'\1\n\2')

# 10. NativePlayerController.kt
replace_regex('composeApp/src/desktopMain/kotlin/com/nuvio/app/features/player/desktop/NativePlayerController.kt',
r'<<<<<<< HEAD.*?KeyboardVolumeDown -> adjustFallbackVolume\(-5f\).*?=======\s*PlayerControlsAction\.KeyboardVolumeDown -> adjustFallbackVolume\(-10f\).*?>>>>>>> origin/Dev',
'''            PlayerControlsAction.KeyboardVolumeDown -> adjustFallbackVolume(-10f)
            PlayerControlsAction.KeyboardVolumeUp -> adjustFallbackVolume(10f)
            PlayerControlsAction.KeyboardToggleMute -> toggleFallbackMute()''')

replace_regex('composeApp/src/desktopMain/kotlin/com/nuvio/app/features/player/desktop/NativePlayerController.kt',
r'<<<<<<< HEAD(.*?)=======\s*@Synchronized\s*>>>>>>> origin/Dev',
r'\1\n    @Synchronized')

# 11. controls.js
replace_regex('composeApp/src/desktopMain/resources/player-ui/controls.js',
r'<<<<<<< HEAD\s*if \(pendingSettingToastCommand !== mappedCommand.*?=======\s*(.*?)\s*>>>>>>> origin/Dev',
'''    if (pendingSettingToastCommand !== mappedCommand || pendingSettingToastToken !== token) return;
    const icon = mappedCommand === "speed" ? "icon-speed" : mappedCommand === "resize" ? "icon-aspect" : null;
    showPlayerToast(settingToastLabel(mappedCommand), { icon });''')

replace_regex('composeApp/src/desktopMain/resources/player-ui/controls.js',
r'<<<<<<< HEAD\s*if \(event\.metaKey \|\| event\.ctrlKey \|\| event\.altKey.*?case "Period": return "keyboardSpeedUp";\s*default: return "";\s*\}\s*\}\s*=======\s*(.*?)\s*>>>>>>> origin/Dev',
'''  if (event.metaKey || event.ctrlKey || event.altKey) return "";
  const isShift = Boolean(event.shiftKey);
  
  if (isShift && (event.code === "KeyN" || event.code === "Comma" || event.code === "Period")) {
    switch (event.code) {
      case "KeyN": return "keyboardNextEpisode";
      case "Comma": return "keyboardSpeedDown";
      case "Period": return "keyboardSpeedUp";
    }
  }''')

# For the rest of controls.js conflicts, we just keep both or combine logic!
replace_regex('composeApp/src/desktopMain/resources/player-ui/controls.js',
r'<<<<<<< HEAD\s*(.*?)\s*=======\s*(.*?)\s*>>>>>>> origin/Dev',
r'\1\n\2')

print("All replacements executed!")
