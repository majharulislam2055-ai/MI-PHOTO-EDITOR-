package com.example.ui.screens.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.BackgroundAdjustments
import com.example.model.BlurIntensity
import com.example.model.PresetRepository
import com.example.model.Strings
import com.example.ui.theme.ElectricViolet

@Composable
fun BackgroundEditorPanel(
    bg: BackgroundAdjustments,
    language: AppLanguage,
    onBgChange: ((BackgroundAdjustments) -> BackgroundAdjustments) -> Unit,
    onPickCustomBg: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        Color(0xFF2563EB) to "Blue",
        Color(0xFF7C3AED) to "Purple",
        Color(0xFF10B981) to "Green",
        Color(0xFFEC4899) to "Pink",
        Color(0xFFF97316) to "Orange",
        Color(0xFFFFFFFF) to "White",
        Color(0xFF0F172A) to "Black",
        Color(0xFFFEF3C7) to "Warm",
        Color(0xFFE0F2FE) to "Cool"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 1. Background Blur
        Text(
            text = Strings.get("bg_blur", language),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val blurOptions = listOf(
                BlurIntensity.NONE to Strings.get("blur_none", language),
                BlurIntensity.LIGHT to Strings.get("blur_light", language),
                BlurIntensity.MEDIUM to Strings.get("blur_med", language),
                BlurIntensity.STRONG to Strings.get("blur_strong", language)
            )

            blurOptions.forEach { (intensity, label) ->
                ElevatedFilterChip(
                    selected = bg.blur == intensity,
                    onClick = { onBgChange { it.copy(blur = intensity) } },
                    label = { Text(label) },
                    colors = FilterChipDefaults.elevatedFilterChipColors(
                        selectedContainerColor = ElectricViolet,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. Remove Background Options
        Text(
            text = Strings.get("bg_remove", language),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ElevatedFilterChip(
                selected = bg.transparentBackground,
                onClick = {
                    onBgChange {
                        it.copy(
                            transparentBackground = !it.transparentBackground,
                            solidColor = null,
                            replacePreset = null
                        )
                    }
                },
                label = { Text(Strings.get("transparent_bg", language)) },
                colors = FilterChipDefaults.elevatedFilterChipColors(
                    selectedContainerColor = ElectricViolet,
                    selectedLabelColor = Color.White
                )
            )

            ElevatedFilterChip(
                selected = bg.solidColor == Color.White,
                onClick = {
                    onBgChange {
                        it.copy(
                            solidColor = if (it.solidColor == Color.White) null else Color.White,
                            transparentBackground = false
                        )
                    }
                },
                label = { Text(Strings.get("white_bg", language)) },
                colors = FilterChipDefaults.elevatedFilterChipColors(
                    selectedContainerColor = ElectricViolet,
                    selectedLabelColor = Color.White
                )
            )

            OutlinedButton(
                onClick = onPickCustomBg,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricViolet)
            ) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(Strings.get("custom_bg", language), fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 3. Background Color Presets
        Text(
            text = Strings.get("bg_color", language),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(colors) { (color, name) ->
                val isSelected = bg.solidColor == color
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) ElectricViolet else Color.Gray.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                        .clickable {
                            onBgChange {
                                it.copy(
                                    solidColor = if (isSelected) null else color,
                                    transparentBackground = false,
                                    replacePreset = null
                                )
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = if (color == Color.White) Color.Black else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 4. Background Replacement Presets (Studio, Office, Nature, Garden, Beach, Sunset, etc.)
        Text(
            text = Strings.get("bg_replace", language),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(PresetRepository.bgPresets) { preset ->
                val isSelected = bg.replacePreset == preset.id
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        onBgChange {
                            it.copy(
                                replacePreset = if (isSelected) null else preset.id,
                                solidColor = null,
                                transparentBackground = false
                            )
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp, 84.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.verticalGradient(preset.previewGradient))
                            .border(
                                width = if (isSelected) 2.5.dp else 0.dp,
                                color = if (isSelected) ElectricViolet else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                        }
                    }
                    Text(
                        text = if (language == AppLanguage.BN) preset.nameBn else preset.nameEn,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
