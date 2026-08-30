with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/collection/FolderDetailScreen.kt', 'r', encoding='utf-8') as f:
    c = f.read()

c = c.replace('''private fun RowsContent(
    uiState: FolderDetailUiState,
    watchedKeys: Set<String>,
    modifier: Modifier = Modifier,
    onCatalogClick: (HomeCatalogSection) -> Unit,
    onPosterClick: (MetaPreview) -> Unit,
) {''', '''private fun RowsContent(
    uiState: FolderDetailUiState,
    watchedKeys: Set<String>,
    modifier: Modifier = Modifier,
    onCatalogClick: (HomeCatalogSection) -> Unit,
    onPosterClick: (MetaPreview) -> Unit,
    homeFocusCoordinator: HomeFocusCoordinator? = null,
) {''')

c = c.replace('''                FolderSection(
                    section = section,
                    entries = if (isDesktop) section.items else section.items.take(catalogPreviewLimit),
                    sectionPadding = sectionPadding,
                    onViewAllClick = if (section.canOpenCatalog(catalogPreviewLimit)) {
                        { onCatalogClick(section) }
                    } else {
                        null
                    },
                    watchedKeys = watchedKeys,
                    onPosterClick = onPosterClick,
                )''', '''                FolderSection(
                    section = section,
                    entries = if (isDesktop) section.items else section.items.take(catalogPreviewLimit),
                    sectionPadding = sectionPadding,
                    onViewAllClick = if (section.canOpenCatalog(catalogPreviewLimit)) {
                        { onCatalogClick(section) }
                    } else {
                        null
                    },
                    watchedKeys = watchedKeys,
                    onPosterClick = onPosterClick,
                    homeFocusCoordinator = homeFocusCoordinator,
                )''')

with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/collection/FolderDetailScreen.kt', 'w', encoding='utf-8') as f:
    f.write(c)
