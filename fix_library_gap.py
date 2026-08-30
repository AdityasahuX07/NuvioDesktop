with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/library/LibraryScreen.kt', 'r', encoding='utf-8') as f:
    c = f.read()

c = c.replace('''        val gridColumns = remember(maxWidth, maxHeight, posterCardStyle.widthDp, isDesktop) {
            if (isDesktop) {
                posterGridColumnCountForViewport(maxWidth, maxHeight, posterCardStyle.widthDp)
            } else {
                posterGridColumnCountForWidth(maxWidth)
            }
        }

        CompositionLocalProvider(''', '''        val gridColumns = remember(maxWidth, maxHeight, posterCardStyle.widthDp, isDesktop) {
            if (isDesktop) {
                posterGridColumnCountForViewport(maxWidth, maxHeight, posterCardStyle.widthDp)
            } else {
                posterGridColumnCountForWidth(maxWidth)
            }
        }
        val libraryStickyHeaderExtraGapPx = with(LocalDensity.current) { com.nuvio.app.core.ui.StickyHeaderExtraGap.roundToPx() }

        CompositionLocalProvider(''')

with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/library/LibraryScreen.kt', 'w', encoding='utf-8') as f:
    f.write(c)
