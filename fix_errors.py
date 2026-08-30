with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/collection/FolderDetailScreen.kt', 'r', encoding='utf-8') as f:
    c = f.read()
c = c.replace('import androidx.compose.runtime.Composable\\\\nimport com.nuvio.app.features.home.HomeFocusCoordinator', 'import androidx.compose.runtime.Composable\\nimport com.nuvio.app.features.home.HomeFocusCoordinator')
with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/collection/FolderDetailScreen.kt', 'w', encoding='utf-8') as f:
    f.write(c)

with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/library/LibraryScreen.kt', 'r', encoding='utf-8') as f:
    c2 = f.read()
c2 = c2.replace('com.nuvio.app.core.ui.StickyHeaderExtraGap', 'com.nuvio.app.core.ui.focus.StickyHeaderExtraGap')
with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/library/LibraryScreen.kt', 'w', encoding='utf-8') as f:
    f.write(c2)
