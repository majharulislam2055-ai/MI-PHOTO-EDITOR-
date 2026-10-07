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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.ui.components.AdjustmentSlider
import com.example.ui.theme.ElectricViolet

@Composable
fun DrawBrushPanel(
    language: AppLanguage,
    onColorSelected: (Color) -> Unit,
    onSizeSelected: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var brushColor by remember { mutableStateOf(Color(0xFFEC4899)) }
    var brushSize by remember { mutableFloatStateOf(16f) }
    var isEraser by remember { mutableStateOf(false) }

    val colors = listOf(
        Color(0xFFEC4899),
        Color(0xFF7C3AED),
        Color(0xFF06B6D4),
        Color(0xFF10B981),
        Color(0xFFFBBF24),
        Color.White,
        Color.Black
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ElevatedFilterChip(
                selected = !isEraser,
                onClick = { isEraser = false },
                label = { Text("Brush") },
                leadingIcon = { Icon(Icons.Default.Brush, contentDescription = null, modifier = Modifier.size(16.dp)) },
                colors = FilterChipDefaults.elevatedFilterChipColors(
                    selectedContainerColor = ElectricViolet,
                    selectedLabelColor = Color.White
                )
            )

            ElevatedFilterChip(
                selected = isEraser,
                onClick = { isEraser = true },
                label = { Text("Eraser") },
                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp)) },
                colors = FilterChipDefaults.elevatedFilterChipColors(
                    selectedContainerColor = ElectricViolet,
                    selectedLabelColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (!isEraser) {
            Text(
                text = "Brush Color",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(colors) { c ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(c)
                            .border(
                                width = if (brushColor == c) 2.5.dp else 1.dp,
                                color = if (brushColor == c) ElectricViolet else Color.Gray,
                                shape = CircleShape
                            )
                            .clickable {
                                brushColor = c
                                onColorSelected(c)
                            }
                    )
                }
            }
        }

        AdjustmentSlider(
            title = "Brush Size",
            value = brushSize,
            valueRange = 4f..60f,
            onValueChange = {
                brushSize = it
                onSizeSelected(it)
            }
        )
    }
}
