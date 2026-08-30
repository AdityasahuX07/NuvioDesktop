with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/home/components/PosterGrid.kt', 'r', encoding='utf-8') as f:
    c = f.read()

c = c.replace('''                                top = 12.dp,
                                start = 16.dp,
                                end = 16.dp,''', '''                                top = 12.dp,
                                start = pageHorizontalPadding,
                                end = pageHorizontalPadding,''')

with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/home/components/PosterGrid.kt', 'w', encoding='utf-8') as f:
    f.write(c)
