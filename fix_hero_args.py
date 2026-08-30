import re

with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/details/components/DesktopDetailHero.kt', 'r', encoding='utf-8') as f:
    c = f.read()

# Remove the incorrectly placed args from DesktopDetailBackdrop
c = c.replace('''    onHeroTrailerError: () -> Unit,

    onPlayClick: () -> Unit,
    onPlayLongClick: (() -> Unit)?,
    onWatchedClick: () -> Unit,
    onSaveClick: () -> Unit,
    onSaveLongClick: (() -> Unit)?,
    playFocusRequester: androidx.compose.ui.focus.FocusRequester? = null,
    modifier: Modifier = Modifier,
) {''', '''    onHeroTrailerError: () -> Unit,
    modifier: Modifier = Modifier,
) {''')

# Add playFocusRequester to DesktopDetailHero
c = c.replace('''    onSaveClick: () -> Unit,
    onSaveLongClick: (() -> Unit)?,
) {''', '''    onSaveClick: () -> Unit,
    onSaveLongClick: (() -> Unit)?,
    playFocusRequester: androidx.compose.ui.focus.FocusRequester? = null,
) {''')

with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/details/components/DesktopDetailHero.kt', 'w', encoding='utf-8') as f:
    f.write(c)
