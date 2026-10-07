package com.example.ui.screens.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.LightingAdjustments
import com.example.model.PresetRepository
import com.example.model.Strings
import com.example.ui.components.AdjustmentSlider
import com.example.ui.theme.ElectricViolet

@Composable
fun LightingEditorPanel(
    lighting: LightingAdjustments,
    language: AppLanguage,
    onLightingChange: ((LightingAdjustments) -> LightingAdjustments) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Lighting Presets",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(PresetRepository.lightingPresets) { preset ->
                ElevatedFilterChip(
                    selected = false,
                    onClick = {
                        onLightingChange {
                            it.copy(
                                brightness = preset.brightness,
                                exposure = preset.exposure,
                                contrast = preset.contrast,
                                temperature = preset.warmth,
                                saturation = preset.saturation
                            )
                        }
                    },
                    label = { Text(if (language == AppLanguage.BN) preset.nameBn else preset.nameEn) },
                    colors = FilterChipDefaults.elevatedFilterChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        AdjustmentSlider(
            title = Strings.get("brightness", language),
            value = lighting.brightness,
            valueRange = -100f..100f,
            onValueChange = { onLightingChange { l -> l.copy(brightness = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("exposure", language),
            value = lighting.exposure,
            valueRange = -100f..100f,
            onValueChange = { onLightingChange { l -> l.copy(exposure = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("contrast", language),
            value = lighting.contrast,
            valueRange = -100f..100f,
            onValueChange = { onLightingChange { l -> l.copy(contrast = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("highlights", language),
            value = lighting.highlights,
            valueRange = -100f..100f,
            onValueChange = { onLightingChange { l -> l.copy(highlights = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("shadows", language),
            value = lighting.shadows,
            valueRange = -100f..100f,
            onValueChange = { onLightingChange { l -> l.copy(shadows = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("temperature", language),
            value = lighting.temperature,
            valueRange = -100f..100f,
            onValueChange = { onLightingChange { l -> l.copy(temperature = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("tint", language),
            value = lighting.tint,
            valueRange = -100f..100f,
            onValueChange = { onLightingChange { l -> l.copy(tint = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("vibrance", language),
            value = lighting.vibrance,
            valueRange = -100f..100f,
            onValueChange = { onLightingChange { l -> l.copy(vibrance = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("saturation", language),
            value = lighting.saturation,
            valueRange = -100f..100f,
            onValueChange = { onLightingChange { l -> l.copy(saturation = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("sharpness", language),
            value = lighting.sharpness,
            valueRange = 0f..100f,
            onValueChange = { onLightingChange { l -> l.copy(sharpness = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("clarity", language),
            value = lighting.clarity,
            valueRange = 0f..100f,
            onValueChange = { onLightingChange { l -> l.copy(clarity = it) } }
        )
    }
}
