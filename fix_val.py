with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/collection/FolderDetailScreen.kt', 'r', encoding='utf-8') as f:
    c = f.read()

c = c.replace('''    var hasRequestedInitialFocus by remember { mutableStateOf(false) }

    val screenFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {''', '''    var hasRequestedInitialFocus by remember { mutableStateOf(false) }

    val screenFocusRequester = remember { FocusRequester() }
    val homeFocusCoordinator = remember { com.nuvio.app.features.home.components.HomeFocusCoordinator() }

    LaunchedEffect(Unit) {''')

with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/collection/FolderDetailScreen.kt', 'w', encoding='utf-8') as f:
    f.write(c)
