with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/collection/FolderDetailScreen.kt', 'r', encoding='utf-8') as f:
    c = f.read()

if 'import com.nuvio.app.features.home.HomeFocusCoordinator' not in c:
    c = c.replace('import androidx.compose.runtime.Composable', 'import androidx.compose.runtime.Composable\\nimport com.nuvio.app.features.home.HomeFocusCoordinator')
    with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/collection/FolderDetailScreen.kt', 'w', encoding='utf-8') as f:
        f.write(c)
