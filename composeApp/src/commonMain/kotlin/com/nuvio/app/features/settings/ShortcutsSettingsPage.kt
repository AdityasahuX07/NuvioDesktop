package com.nuvio.app.features.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nuvio.app.core.ui.nuvio
import nuvio.composeapp.generated.resources.Res
import nuvio.composeapp.generated.resources.settings_shortcuts_close_player
import nuvio.composeapp.generated.resources.settings_shortcuts_go_back
import nuvio.composeapp.generated.resources.settings_shortcuts_go_home
import nuvio.composeapp.generated.resources.settings_shortcuts_go_library
import nuvio.composeapp.generated.resources.settings_shortcuts_go_search
import nuvio.composeapp.generated.resources.settings_shortcuts_go_settings
import nuvio.composeapp.generated.resources.settings_shortcuts_key_combo_separator
import nuvio.composeapp.generated.resources.settings_shortcuts_navigate_focus
import nuvio.composeapp.generated.resources.settings_shortcuts_navigate_options
import nuvio.composeapp.generated.resources.settings_shortcuts_open_search_focused
import nuvio.composeapp.generated.resources.settings_shortcuts_section_general_navigation
import nuvio.composeapp.generated.resources.settings_shortcuts_section_player_playback
import nuvio.composeapp.generated.resources.settings_shortcuts_switch_collection_tabs
import nuvio.composeapp.generated.resources.settings_shortcuts_toggle_fullscreen
import nuvio.composeapp.generated.resources.settings_shortcuts_audio_selector
import nuvio.composeapp.generated.resources.settings_shortcuts_subtitle_selector
import nuvio.composeapp.generated.resources.settings_shortcuts_episode_list
import nuvio.composeapp.generated.resources.settings_shortcuts_source_list
import nuvio.composeapp.generated.resources.settings_shortcuts_toggle_subtitle
import nuvio.composeapp.generated.resources.settings_shortcuts_sequential_audio
import nuvio.composeapp.generated.resources.settings_shortcuts_hold_to_speed
import nuvio.composeapp.generated.resources.settings_shortcuts_skip_intro
import nuvio.composeapp.generated.resources.settings_shortcuts_next_episode
import nuvio.composeapp.generated.resources.settings_shortcuts_playback_speed
import nuvio.composeapp.generated.resources.settings_shortcuts_reset_playback_speed
import nuvio.composeapp.generated.resources.settings_shortcuts_play_pause
import nuvio.composeapp.generated.resources.settings_shortcuts_subtitle_delay
import nuvio.composeapp.generated.resources.settings_shortcuts_toggle_mute
import nuvio.composeapp.generated.resources.settings_shortcuts_volume_control
import nuvio.composeapp.generated.resources.settings_shortcuts_toggle_sub_opacity
import nuvio.composeapp.generated.resources.settings_shortcuts_sub_opacity_control
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/** Key-cap tokens rendered as arrow icons (rather than text) so all four glyphs share one size. */
private const val KeyArrowUp = "\u2191"
private const val KeyArrowDown = "\u2193"
private const val KeyArrowLeft = "\u2190"
private const val KeyArrowRight = "\u2192"

private val ArrowKeyIcons: Map<String, ImageVector> = mapOf(
    KeyArrowUp to Icons.Rounded.KeyboardArrowUp,
    KeyArrowDown to Icons.Rounded.KeyboardArrowDown,
    KeyArrowLeft to Icons.Rounded.KeyboardArrowLeft,
    KeyArrowRight to Icons.Rounded.KeyboardArrowRight,
)

/**
 * A single keyboard/mouse shortcut entry.
 *
 * [combos] is a list of alternative key combinations that all trigger the action
 * (rendered joined by "or"). Each combo is itself an ordered list of keys rendered
 * joined by "+" (e.g. listOf("Shift", "/") renders as "Shift + /").
 */
internal data class ShortcutEntry(
    val titleRes: StringResource,
    val combos: List<List<String>>,
)

private val generalNavigationShortcuts: List<ShortcutEntry> = listOf(
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_navigate_focus,
        combos = listOf(
            listOf(KeyArrowUp),
            listOf(KeyArrowDown),
            listOf(KeyArrowLeft),
            listOf(KeyArrowRight),
        ),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_switch_collection_tabs,
        combos = listOf(listOf("Q"), listOf("E")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_go_home,
        combos = listOf(listOf("1")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_go_search,
        combos = listOf(listOf("2")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_go_library,
        combos = listOf(listOf("3")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_go_settings,
        combos = listOf(listOf("4")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_open_search_focused,
        combos = listOf(listOf("0"), listOf("/")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_toggle_fullscreen,
        combos = listOf(listOf("F"), listOf("F11")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_go_back,
        combos = listOf(listOf("Esc"), listOf("Backspace")),
    ),
)
// Populated incrementally — add entries here as each shortcut is wired up in the app.
// Until entries are added, the corresponding section is simply not shown.
private val playerPlaybackShortcuts: List<ShortcutEntry> = listOf(
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_play_pause,
        combos = listOf(listOf("Space"), listOf("Right Click")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_close_player,
        combos = listOf(listOf("Esc")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_navigate_options,
        combos = listOf(
            listOf(KeyArrowUp),
            listOf(KeyArrowDown),
            listOf(KeyArrowLeft),
            listOf(KeyArrowRight),
        ),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_audio_selector,
        combos = listOf(listOf("A")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_subtitle_selector,
        combos = listOf(listOf("S")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_episode_list,
        combos = listOf(listOf("E")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_source_list,
        combos = listOf(listOf("Q")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_toggle_subtitle,
        combos = listOf(listOf("V")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_sequential_audio,
        combos = listOf(listOf("B")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_hold_to_speed,
        combos = listOf(listOf("Hold Space"), listOf("Hold Left Click")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_skip_intro,
        combos = listOf(listOf("Enter")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_next_episode,
        combos = listOf(listOf("Shift", "N")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_playback_speed,
        combos = listOf(listOf("<"), listOf(">")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_reset_playback_speed,
        combos = listOf(listOf("/")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_subtitle_delay,
        combos = listOf(listOf("G"), listOf("H")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_toggle_mute,
        combos = listOf(listOf("M")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_volume_control,
        combos = listOf(
            listOf(KeyArrowUp),
            listOf(KeyArrowDown),
            listOf("Scroll Up"),
            listOf("Scroll Down"),
        ),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_toggle_sub_opacity,
        combos = listOf(listOf("O")),
    ),
    ShortcutEntry(
        titleRes = Res.string.settings_shortcuts_sub_opacity_control,
        combos = listOf(listOf("P"), listOf("I")),
    ),
)

internal fun LazyListScope.shortcutsSettingsContent(isTablet: Boolean) {
    shortcutsSection(
        titleRes = Res.string.settings_shortcuts_section_general_navigation,
        entries = generalNavigationShortcuts,
        isTablet = isTablet,
    )
    shortcutsSection(
        titleRes = Res.string.settings_shortcuts_section_player_playback,
        entries = playerPlaybackShortcuts,
        isTablet = isTablet,
    )
}

private fun LazyListScope.shortcutsSection(
    titleRes: StringResource,
    entries: List<ShortcutEntry>,
    isTablet: Boolean,
) {
    if (entries.isEmpty()) return
    item {
        SettingsSection(
            title = stringResource(titleRes),
            isTablet = isTablet,
        ) {
            SettingsGroup(isTablet = isTablet) {
                entries.forEachIndexed { index, entry ->
                    SettingsShortcutRow(
                        title = stringResource(entry.titleRes),
                        combos = entry.combos,
                        isTablet = isTablet,
                    )
                    if (index != entries.lastIndex) {
                        SettingsGroupDivider(isTablet = isTablet)
                    }
                }
            }
        }
    }
}

@Composable
internal fun SettingsShortcutRow(
    title: String,
    combos: List<List<String>>,
    isTablet: Boolean,
) {
    val tokens = MaterialTheme.nuvio
    val verticalPadding = if (isTablet) 16.dp else 14.dp
    val horizontalPadding = if (isTablet) 20.dp else 16.dp
    val orLabel = stringResource(Res.string.settings_shortcuts_key_combo_separator)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = tokens.colors.textPrimary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            combos.forEachIndexed { comboIndex, combo ->
                if (comboIndex != 0) {
                    Text(
                        text = orLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = tokens.colors.textMuted,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    combo.forEachIndexed { keyIndex, key ->
                        if (keyIndex != 0) {
                            Text(
                                text = "+",
                                style = MaterialTheme.typography.bodySmall,
                                color = tokens.colors.textMuted,
                            )
                        }
                        ShortcutKeyCap(text = key)
                    }
                }
            }
        }
    }
}

@Composable
private fun ShortcutKeyCap(text: String) {
    val tokens = MaterialTheme.nuvio
    val arrowIcon = ArrowKeyIcons[text]
    Surface(
        color = tokens.colors.surfaceCard,
        shape = tokens.shapes.compactCard,
        border = BorderStroke(tokens.borders.hairline, tokens.colors.borderDefault),
    ) {
        Box(
            modifier = Modifier
                .defaultMinSize(minWidth = 30.dp, minHeight = 26.dp)
                .padding(horizontal = if (arrowIcon != null) 7.dp else 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (arrowIcon != null) {
                // Every arrow direction shares this exact icon + box size so the four
                // glyphs never look mismatched next to each other.
                Icon(
                    imageVector = arrowIcon,
                    contentDescription = null,
                    tint = tokens.colors.textPrimary,
                    modifier = Modifier.size(16.dp),
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.Monospace,
                    color = tokens.colors.textPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
