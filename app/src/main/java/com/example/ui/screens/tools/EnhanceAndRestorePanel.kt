package com.example.ui.screens.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.Strings
import com.example.ui.components.GradientButton
import com.example.ui.theme.ElectricViolet

@Composable
fun EnhanceAndRestorePanel(
    language: AppLanguage,
    onRestoreOldPhoto: (Boolean) -> Unit,
    onUpscale: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedUpscale by remember { mutableStateOf("1080p") }
    var colorizeBw by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "AI Image Enhancer & Upscale",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val upscaleOptions = listOf("1080p (HD)", "2K (Full HD)", "4K (Ultra HD)")
            upscaleOptions.forEach { opt ->
                ElevatedFilterChip(
                    selected = selectedUpscale == opt,
                    onClick = {
                        selectedUpscale = opt
                        val factor = if (opt.startsWith("4K")) 4 else if (opt.startsWith("2K")) 2 else 1
                        onUpscale(factor)
                    },
                    label = { Text(opt) },
                    colors = FilterChipDefaults.elevatedFilterChipColors(
                        selectedContainerColor = ElectricViolet,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Old Photo Restoration",
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
                selected = colorizeBw,
                onClick = { colorizeBw = !colorizeBw },
                label = { Text(Strings.get("bw_to_color", language)) },
                colors = FilterChipDefaults.elevatedFilterChipColors(
                    selectedContainerColor = ElectricViolet,
                    selectedLabelColor = Color.White
                )
            )

            ElevatedFilterChip(
                selected = true,
                onClick = { },
                label = { Text(Strings.get("scratch_remove", language)) },
                colors = FilterChipDefaults.elevatedFilterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    selectedLabelColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        GradientButton(
            text = "Restore Old Photo Now",
            onClick = { onRestoreOldPhoto(colorizeBw) },
            icon = {
                Icon(
                    imageVector = Icons.Default.HistoryEdu,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        )
    }
}
