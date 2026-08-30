with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/streams/StreamsTabletLayout.kt', 'r', encoding='utf-8') as f:
    c = f.read()

c = c.replace('''                    StreamList(
                        uiState = uiState,
                        debridEnabled = debridEnabled,
                        appendInstantServiceToDefaultName = appendInstantServiceToDefaultName,
                        onStreamSelected = onStreamSelected,
                        onStreamLongPress = onStreamLongPress,
                        onStreamSecondaryClick = onStreamSecondaryClick,
                        resumePositionMs = resumePositionMs,
                        resumeProgressFraction = resumeProgressFraction,
                        modifier = Modifier.weight(1f),
                    )''', '''                    StreamList(
                        uiState = uiState,
                        debridEnabled = debridEnabled,
                        appendInstantServiceToDefaultName = appendInstantServiceToDefaultName,
                        onStreamSelected = onStreamSelected,
                        onStreamLongPress = onStreamLongPress,
                        onStreamSecondaryClick = onStreamSecondaryClick,
                        resumePositionMs = resumePositionMs,
                        resumeProgressFraction = resumeProgressFraction,
                        listState = listState,
                        streamSections = streamSections,
                        modifier = Modifier.weight(1f),
                    )''')

with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/streams/StreamsTabletLayout.kt', 'w', encoding='utf-8') as f:
    f.write(c)
