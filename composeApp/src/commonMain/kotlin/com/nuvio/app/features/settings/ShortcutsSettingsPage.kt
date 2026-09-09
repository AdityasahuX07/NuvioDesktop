package com.nuvio.app.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nuvio.app.core.ui.nuvio
import nuvio.composeapp.generated.resources.*

internal fun LazyListScope.shortcutsSettingsContent(
    isTablet: Boolean,
) {
    item {
        SettingsSection(
            title = "GENERAL & NAVIGATION",
            isTablet = isTablet,
        ) {
            SettingsGroup(isTablet = isTablet) {
                ShortcutRow("Go back / previous screen", isTablet) {
                    ShortcutKey("Esc")
                    ShortcutOrText()
                    ShortcutKey("Backspace")
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.Backward, "Backward")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Show options", isTablet) {
                    ShortcutMouseKey(MouseButton.Right, "Right")
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.Left, "Hold Left")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Switch to home", isTablet) {
                    ShortcutKey("1")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Switch to search", isTablet) {
                    ShortcutKey("2")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Switch to library", isTablet) {
                    ShortcutKey("3")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Switch to settings", isTablet) {
                    ShortcutKey("4")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Activate search box", isTablet) {
                    ShortcutKey("/")
                    ShortcutOrText()
                    ShortcutKey("0")
                }
            }
        }
    }
    
    item {
        SettingsSection(
            title = "PLAYER \u2013 PLAYBACK",
            isTablet = isTablet,
        ) {
            SettingsGroup(isTablet = isTablet) {
                ShortcutRow("Play / Pause", isTablet) {
                    ShortcutKey("Space")
                    ShortcutOrText()
                    ShortcutKey("K")
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.Left, "Left")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Sequential switch audio track", isTablet) {
                    ShortcutKey("B")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Toggle enable / disable subtitle", isTablet) {
                    ShortcutKey("V")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Toggle mute state", isTablet) {
                    ShortcutKey("M")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Seek forward or backward", isTablet) {
                    ShortcutIconKey(Icons.Default.KeyboardArrowLeft)
                    ShortcutOrText()
                    ShortcutIconKey(Icons.Default.KeyboardArrowRight)
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.Forward, "Forward")
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.Backward, "Backward")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Volume up / down", isTablet) {
                    ShortcutIconKey(Icons.Default.KeyboardArrowUp)
                    ShortcutOrText()
                    ShortcutIconKey(Icons.Default.KeyboardArrowDown)
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.ScrollUp, "Scroll Up")
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.ScrollDown, "Scroll Down")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Toggle app fullscreen", isTablet) {
                    ShortcutKey("F11")
                    ShortcutOrText()
                    ShortcutKey("F")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Skip intro", isTablet) {
                    ShortcutKey("Enter")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Open audio selector window", isTablet) {
                    ShortcutKey("A")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Open subtitle selector window", isTablet) {
                    ShortcutKey("S")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Open source list window", isTablet) {
                    ShortcutKey("Q")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Open episode list window", isTablet) {
                    ShortcutKey("E")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Move focus and options", isTablet) {
                    ShortcutIconKey(Icons.Default.KeyboardArrowUp)
                    ShortcutOrText()
                    ShortcutIconKey(Icons.Default.KeyboardArrowDown)
                    ShortcutOrText()
                    ShortcutIconKey(Icons.Default.KeyboardArrowLeft)
                    ShortcutOrText()
                    ShortcutIconKey(Icons.Default.KeyboardArrowRight)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Play next episode", isTablet) {
                    ShortcutKey("Shift")
                    ShortcutPlusText()
                    ShortcutKey("N")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Hold to speed up playback", isTablet) {
                    ShortcutKey("Hold Space")
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.Left, "Hold Left")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Open playback speed panel", isTablet) {
                    ShortcutKey("`")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Playback speed control", isTablet) {
                    ShortcutKey("Shift")
                    ShortcutPlusText()
                    ShortcutKey("<")
                    ShortcutOrText()
                    ShortcutKey("Shift")
                    ShortcutPlusText()
                    ShortcutKey(">")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Reset playback speed to 1x", isTablet) {
                    ShortcutKey("/")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Decrease / increase subtitle delay", isTablet) {
                    ShortcutKey("G")
                    ShortcutOrText()
                    ShortcutKey("H")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Turn on / off sub opacity", isTablet) {
                    ShortcutKey("O")
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow("Increase / reduce opacity of subs", isTablet) {
                    ShortcutKey("I")
                    ShortcutOrText()
                    ShortcutKey("P")
                }
            }
        }
    }
}

@Composable
private fun ShortcutRow(
    title: String,
    isTablet: Boolean,
    keys: @Composable RowScope.() -> Unit,
) {
    val tokens = MaterialTheme.nuvio
    val verticalPadding = if (isTablet) 16.dp else 14.dp
    val horizontalPadding = if (isTablet) 20.dp else 16.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = tokens.colors.textPrimary,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            keys()
        }
    }
}

@Composable
private fun ShortcutKey(label: String) {
    val tokens = MaterialTheme.nuvio
    Box(
        modifier = Modifier
            .background(
                color = tokens.colors.surfaceCard,
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.dp,
                color = tokens.colors.borderSubtle,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = tokens.colors.textPrimary,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
        )
    }
}

@Composable
private fun ShortcutIconKey(icon: androidx.compose.ui.graphics.vector.ImageVector) {
    val tokens = MaterialTheme.nuvio
    Box(
        modifier = Modifier
            .background(
                color = tokens.colors.surfaceCard,
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.dp,
                color = tokens.colors.borderSubtle,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tokens.colors.textPrimary,
            modifier = Modifier.size(18.dp)
        )
    }
}

enum class MouseButton { Left, Right, Middle, ScrollUp, ScrollDown, Forward, Backward, None }

@Composable
private fun ShortcutMouseKey(button: MouseButton, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.nuvio.colors.borderSubtle,
                shape = RoundedCornerShape(6.dp)
            )
            .background(
                color = MaterialTheme.nuvio.colors.surfaceCard,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 6.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val iconRes = when (button) {
                MouseButton.Left -> Res.drawable.mouse_left_button_16
                MouseButton.Right -> Res.drawable.mouse_right_button_16
                MouseButton.Middle, MouseButton.ScrollUp, MouseButton.ScrollDown -> Res.drawable.mouse_middle_button_16
                MouseButton.Forward -> Res.drawable.mouse_m4_button_16
                MouseButton.Backward -> Res.drawable.mouse_m5_button_16
                MouseButton.None -> null
            }
            
            if (iconRes != null) {
                Icon(
                    painter = org.jetbrains.compose.resources.painterResource(iconRes),
                    contentDescription = null,
                    tint = MaterialTheme.nuvio.colors.textPrimary,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Box(modifier = Modifier.size(16.dp))
            }
            
            Text(
                text = label,
                color = MaterialTheme.nuvio.colors.textPrimary,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ShortcutOrText() {
    Text(
        text = "or",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.nuvio.colors.textSecondary,
        modifier = Modifier.padding(horizontal = 2.dp)
    )
}

@Composable
private fun ShortcutPlusText() {
    Text(
        text = "+",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.nuvio.colors.textSecondary,
        modifier = Modifier.padding(horizontal = 2.dp)
    )
}

