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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.FilterAdjustment
import com.example.model.PresetRepository
import com.example.ui.components.AdjustmentSlider
import com.example.ui.theme.ElectricViolet

@Composable
fun FilterEditorPanel(
    filter: FilterAdjustment,
    language: AppLanguage,
    onFilterChange: (String, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Natural") }
    val categories = listOf("Natural", "Portrait", "Cinematic", "Color")

    val categoryFilters = PresetRepository.filters.filter { it.category == selectedCategory }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Category Selector Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                ElevatedFilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat) },
                    colors = FilterChipDefaults.elevatedFilterChipColors(
                        selectedContainerColor = ElectricViolet,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal filter cards
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            // "None" option
            item {
                val isSelected = filter.filterId == "none"
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onFilterChange("none", filter.intensity) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp, 80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                width = if (isSelected) 2.5.dp else 0.dp,
                                color = if (isSelected) ElectricViolet else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("None", style = MaterialTheme.typography.labelMedium)
                    }
                    Text("Original", fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }

            items(categoryFilters) { item ->
                val isSelected = filter.filterId == item.id
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onFilterChange(item.id, filter.intensity) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp, 80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(item.previewColor)
                            .border(
                                width = if (isSelected) 2.5.dp else 0.dp,
                                color = if (isSelected) ElectricViolet else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == AppLanguage.BN) item.nameBn.take(2) else item.nameEn.take(2),
                            color = Color.Black.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Text(
                        text = if (language == AppLanguage.BN) item.nameBn else item.nameEn,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filter.filterId != "none") {
            AdjustmentSlider(
                title = "Filter Intensity",
                value = filter.intensity,
                valueRange = 0f..100f,
                onValueChange = { onFilterChange(filter.filterId, it) }
            )
        }
    }
}
