import re
with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/home/components/HomeHeroSection.kt', 'r', encoding='utf-8') as f:
    c = f.read()
c = c.replace('''            FullscreenActionButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = space.s32,
                        end = contentHorizontalPadding,
                    ),''',
'''            val homeFocusCoordinatorForFullscreen = LocalHomeFocusCoordinator.current
            FullscreenActionButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = space.s32,
                        end = contentHorizontalPadding,
                    )
                    .then(
                        if (homeFocusCoordinatorForFullscreen != null) {
                            Modifier.focusProperties {
                                down = homeFocusCoordinatorForFullscreen.heroViewDetailsFocusRequester
                            }
                        } else {
                            Modifier
                        }
                    ),''')
with open('composeApp/src/commonMain/kotlin/com/nuvio/app/features/home/components/HomeHeroSection.kt', 'w', encoding='utf-8') as f:
    f.write(c)
