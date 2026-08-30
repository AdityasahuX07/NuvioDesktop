with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/streams/StreamsTabletLayout.kt', 'r', encoding='utf-8') as f:
    c = f.read()

c = c.replace('''            onStreamSecondaryClick = onStreamSecondaryClick,
            onRefresh = onRefresh,
            modifier = modifier,
        )
        return
    }''', '''            onStreamSecondaryClick = onStreamSecondaryClick,
            onRefresh = onRefresh,
            listState = listState,
            streamSections = streamSections,
            modifier = modifier,
        )
        return
    }''')

with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/streams/StreamsTabletLayout.kt', 'w', encoding='utf-8') as f:
    f.write(c)
