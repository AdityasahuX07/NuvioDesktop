with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/collection/FolderDetailScreen.kt', 'r', encoding='utf-8') as f:
    c = f.read()

c = c.replace('import com.nuvio.app.features.home.HomeFocusCoordinator', 'import com.nuvio.app.features.home.components.HomeFocusCoordinator')

with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/collection/FolderDetailScreen.kt', 'w', encoding='utf-8') as f:
    f.write(c)
