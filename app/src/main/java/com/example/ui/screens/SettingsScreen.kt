package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Support
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Strings
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonCyan

@Composable
fun SettingsScreen(
    language: AppLanguage,
    isDarkMode: Boolean,
    isAiAutoMode: Boolean,
    onToggleLanguage: () -> Unit,
    onToggleDarkMode: () -> Unit,
    onToggleAiAutoMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    var autoSaveEnabled by remember { mutableStateOf(true) }
    var selectedQualityEngine by remember { mutableStateOf("High Precision") }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
                .padding(bottom = 70.dp)
        ) {
            Text(
                text = Strings.get("settings_title", language),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Account Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(BrandGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = Strings.get("creator_profile", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ElectricViolet)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = Strings.get("pro_status", language),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. App Settings Section
            SectionHeader(title = Strings.get("app_settings", language))

            SettingSwitchItem(
                icon = Icons.Default.Language,
                title = Strings.get("language_setting", language),
                subtitle = if (language == AppLanguage.BN) "বাংলা (Bengali)" else "English",
                isChecked = language == AppLanguage.BN,
                onCheckedChange = { onToggleLanguage() },
                testTag = "setting_language_toggle"
            )

            SettingSwitchItem(
                icon = Icons.Default.DarkMode,
                title = Strings.get("theme_setting", language),
                subtitle = if (isDarkMode) "Dark (অন্ধকার)" else "Light (উজ্জ্বল)",
                isChecked = isDarkMode,
                onCheckedChange = { onToggleDarkMode() },
                testTag = "setting_dark_mode_toggle"
            )

            SettingSwitchItem(
                icon = Icons.Default.Save,
                title = Strings.get("auto_save", language),
                subtitle = "Save edited versions to local history",
                isChecked = autoSaveEnabled,
                onCheckedChange = { autoSaveEnabled = it },
                testTag = "setting_auto_save_toggle"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 3. AI Settings Section
            SectionHeader(title = Strings.get("ai_settings", language))

            SettingSwitchItem(
                icon = Icons.Default.AutoAwesome,
                title = Strings.get("ai_auto_mode", language),
                subtitle = Strings.get("ai_auto_desc", language),
                isChecked = isAiAutoMode,
                onCheckedChange = { onToggleAiAutoMode() },
                testTag = "setting_ai_auto_toggle"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HighQuality, contentDescription = null, tint = ElectricViolet)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = Strings.get("ai_quality", language),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Balanced", "High Precision").forEach { engine ->
                            ElevatedFilterChip(
                                selected = selectedQualityEngine == engine,
                                onClick = { selectedQualityEngine = engine },
                                label = { Text(engine) },
                                colors = FilterChipDefaults.elevatedFilterChipColors(
                                    selectedContainerColor = ElectricViolet,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Privacy & Data Safety Section
            SectionHeader(title = Strings.get("privacy_policy", language))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = NeonCyan)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Zero Data Leak / Private Storage",
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = Strings.get("privacy_desc", language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. About & Version
            SectionHeader(title = Strings.get("about_app", language))

            Text(
                text = "MI PHOTO EDITOR\nEdit Smarter. Look Better.\n${Strings.get("version", language)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = ElectricViolet,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SettingSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String = "setting_switch"
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ElectricViolet,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = ElectricViolet
                ),
                modifier = Modifier.testTag(testTag)
            )
        }
    }
}
