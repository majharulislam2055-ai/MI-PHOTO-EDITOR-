package com.example.ui.screens.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Rotate90DegreesCw
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.CropTransform
import com.example.ui.theme.ElectricViolet

@Composable
fun CropTransformPanel(
    crop: CropTransform,
    language: AppLanguage,
    onRotate: () -> Unit,
    onFlipH: () -> Unit,
    onFlipV: () -> Unit,
    onRatioChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val ratios = listOf(
        "Free", "1:1 (Square)", "4:5 (Insta)", "3:4 (Portrait)", "16:9 (Landscape)", "9:16 (Story/Reel)"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Aspect Ratio & Social Presets",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ratios) { ratio ->
                val ratioKey = ratio.split(" ").first()
                ElevatedFilterChip(
                    selected = crop.aspectRatio == ratioKey,
                    onClick = { onRatioChange(ratioKey) },
                    label = { Text(ratio) },
                    colors = FilterChipDefaults.elevatedFilterChipColors(
                        selectedContainerColor = ElectricViolet,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Transform Tools",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onRotate,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricViolet)
            ) {
                Icon(Icons.Default.Rotate90DegreesCw, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.size(4.dp))
                Text("90°")
            }

            OutlinedButton(
                onClick = onFlipH,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricViolet)
            ) {
                Icon(Icons.Default.Flip, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.size(4.dp))
                Text("Flip H")
            }

            OutlinedButton(
                onClick = onFlipV,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricViolet)
            ) {
                Icon(Icons.Default.Flip, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.size(4.dp))
                Text("Flip V")
            }
        }
    }
}
