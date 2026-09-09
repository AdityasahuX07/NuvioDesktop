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
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

internal fun LazyListScope.shortcutsSettingsContent(
    isTablet: Boolean,
) {
    item {
        SettingsSection(
            title = stringResource(Res.string.shortcuts_section_general_navigation),
            isTablet = isTablet,
        ) {
            SettingsGroup(isTablet = isTablet) {
                ShortcutRow(Res.string.shortcuts_go_back, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_esc)
                    ShortcutOrText()
                    ShortcutKey(Res.string.shortcuts_key_backspace)
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.Backward, Res.string.shortcuts_mouse_backward)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_show_options, isTablet) {
                    ShortcutMouseKey(MouseButton.Right, Res.string.shortcuts_mouse_right)
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.Left, Res.string.shortcuts_mouse_hold_left)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_switch_home, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_1)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_switch_search, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_2)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_switch_library, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_3)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_switch_settings, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_4)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_activate_search, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_slash)
                    ShortcutOrText()
                    ShortcutKey(Res.string.shortcuts_key_0)
                }
            }
        }
    }
    
    item {
        SettingsSection(
            title = stringResource(Res.string.shortcuts_section_player_playback),
            isTablet = isTablet,
        ) {
            SettingsGroup(isTablet = isTablet) {
                ShortcutRow(Res.string.shortcuts_play_pause, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_space)
                    ShortcutOrText()
                    ShortcutKey(Res.string.shortcuts_key_k)
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.Left, Res.string.shortcuts_mouse_left)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_switch_audio_track, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_b)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_toggle_subtitle, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_v)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_toggle_mute, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_m)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_seek, isTablet) {
                    ShortcutIconKey(Icons.Default.KeyboardArrowLeft)
                    ShortcutOrText()
                    ShortcutIconKey(Icons.Default.KeyboardArrowRight)
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.Forward, Res.string.shortcuts_mouse_forward)
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.Backward, Res.string.shortcuts_mouse_backward)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_volume, isTablet) {
                    ShortcutIconKey(Icons.Default.KeyboardArrowUp)
                    ShortcutOrText()
                    ShortcutIconKey(Icons.Default.KeyboardArrowDown)
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.ScrollUp, Res.string.shortcuts_mouse_scroll_up)
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.ScrollDown, Res.string.shortcuts_mouse_scroll_down)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_toggle_fullscreen, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_f11)
                    ShortcutOrText()
                    ShortcutKey(Res.string.shortcuts_key_f)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_skip_intro, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_enter)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_open_audio_selector, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_a)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_open_subtitle_selector, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_s)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_open_source_list, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_q)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_open_episode_list, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_e)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_move_focus, isTablet) {
                    ShortcutIconKey(Icons.Default.KeyboardArrowUp)
                    ShortcutOrText()
                    ShortcutIconKey(Icons.Default.KeyboardArrowDown)
                    ShortcutOrText()
                    ShortcutIconKey(Icons.Default.KeyboardArrowLeft)
                    ShortcutOrText()
                    ShortcutIconKey(Icons.Default.KeyboardArrowRight)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_play_next_episode, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_shift)
                    ShortcutPlusText()
                    ShortcutKey(Res.string.shortcuts_key_n)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_hold_speed, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_hold_space)
                    ShortcutOrText()
                    ShortcutMouseKey(MouseButton.Left, Res.string.shortcuts_mouse_hold_left)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_open_speed_panel, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_backtick)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_speed_control, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_shift)
                    ShortcutPlusText()
                    ShortcutKey(Res.string.shortcuts_key_less_than)
                    ShortcutOrText()
                    ShortcutKey(Res.string.shortcuts_key_shift)
                    ShortcutPlusText()
                    ShortcutKey(Res.string.shortcuts_key_greater_than)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_reset_speed, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_slash)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_subtitle_delay, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_g)
                    ShortcutOrText()
                    ShortcutKey(Res.string.shortcuts_key_h)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_toggle_subtitle_opacity, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_o)
                }
                SettingsGroupDivider(isTablet = isTablet)
                ShortcutRow(Res.string.shortcuts_adjust_subtitle_opacity, isTablet) {
                    ShortcutKey(Res.string.shortcuts_key_i)
                    ShortcutOrText()
                    ShortcutKey(Res.string.shortcuts_key_p)
                }
            }
        }
    }
}

@Composable
private fun ShortcutRow(
    title: StringResource,
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
            text = stringResource(title),
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
private fun ShortcutKey(label: StringResource) {
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
            text = stringResource(label),
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
private fun ShortcutMouseKey(
    button: MouseButton,
    label: StringResource,
    modifier: Modifier = Modifier,
) {
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
                text = stringResource(label),
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
        text = stringResource(Res.string.shortcuts_or),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.nuvio.colors.textSecondary,
        modifier = Modifier.padding(horizontal = 2.dp)
    )
}

@Composable
private fun ShortcutPlusText() {
    Text(
        text = stringResource(Res.string.shortcuts_plus),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.nuvio.colors.textSecondary,
        modifier = Modifier.padding(horizontal = 2.dp)
    )
}
