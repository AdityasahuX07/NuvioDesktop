with open('composeApp/src/desktopMain/resources/player-ui/controls.js', 'r', encoding='utf-8') as f:
    c = f.read()

bad_keydown = '''  if (event.code === "Space" && !event.shiftKey && !event.ctrlKey && !event.metaKey && !event.altKey) {
    event.preventDefault();
    if (!event.repeat && !window.spaceHoldTimer) {
      window.spaceHoldTimer = setTimeout(() => {
        send("keyboardActivateHoldToSpeed", 0);
        window.isSpaceHoldingToSpeed = true;
      }, 500);
    }
    return;
  }\n'''

bad_keyup = '''document.addEventListener("keyup", event => {
  if (event.code === "Space" && !event.shiftKey && !event.ctrlKey && !event.metaKey && !event.altKey) {
    if (window.spaceHoldTimer) {
      clearTimeout(window.spaceHoldTimer);
      window.spaceHoldTimer = null;
    }
    if (window.isSpaceHoldingToSpeed) {
      send("keyboardDeactivateHoldToSpeed", 0);
      window.isSpaceHoldingToSpeed = false;
    } else {
      if (!isTextEntryTarget(event.target)) {
        requestPlaybackState("setPlaybackStateQuiet", false);
      }
    }
  }
});\n\n'''

if bad_keydown in c:
    print('Found bad keydown')
    c = c.replace(bad_keydown, '')
else:
    print('Bad keydown NOT FOUND')

if bad_keyup in c:
    print('Found bad keyup')
    c = c.replace(bad_keyup, '')
else:
    print('Bad keyup NOT FOUND')

with open('composeApp/src/desktopMain/resources/player-ui/controls.js', 'w', encoding='utf-8') as f:
    f.write(c)
