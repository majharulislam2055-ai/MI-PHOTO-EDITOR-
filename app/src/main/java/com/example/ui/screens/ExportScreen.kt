package com.example.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Strings
import com.example.ui.components.AdjustmentSlider
import com.example.ui.components.GradientButton
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonCyan
import com.example.viewmodel.EditorViewModel
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.roundToInt

@Composable
fun ExportScreen(
    editorViewModel: EditorViewModel,
    language: AppLanguage,
    onBack: () -> Unit,
    onPhotoSaved: (File) -> Unit,
    onShareClick: (File) -> Unit,
    modifier: Modifier = Modifier
) {
    val displayedBitmap = editorViewModel.displayedBitmap.value
    var selectedFormat by remember { mutableStateOf("JPG") }
    var selectedQualityTier by remember { mutableStateOf("Full HD") }
    var qualityPercent by remember { mutableFloatStateOf(95f) }
    var savedFile by remember { mutableStateOf<File?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("export_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = Strings.get("export_title", language),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Centered Image Preview Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (displayedBitmap != null) {
                    Image(
                        bitmap = displayedBitmap.asImageBitmap(),
                        contentDescription = "Export preview",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // File Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(Strings.get("resolution_label", language), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = if (displayedBitmap != null) "${displayedBitmap.width} × ${displayedBitmap.height}" else "1080 × 1440",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(Strings.get("format_label", language), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(selectedFormat, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(Strings.get("size_label", language), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        val estSizeMb = ((displayedBitmap?.width ?: 1080) * (displayedBitmap?.height ?: 1440) * 4 / (1024f * 1024f) * (qualityPercent / 200f))
                        Text(String.format("%.1f MB", estSizeMb), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quality Tiers
            Text(
                text = Strings.get("quality_label", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Standard", "HD", "Full HD", "Ultra HD").forEach { tier ->
                    ElevatedFilterChip(
                        selected = selectedQualityTier == tier,
                        onClick = { selectedQualityTier = tier },
                        label = { Text(tier) },
                        colors = FilterChipDefaults.elevatedFilterChipColors(
                            selectedContainerColor = ElectricViolet,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Format Selection
            Text(
                text = Strings.get("format_label", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("JPG", "PNG", "WEBP").forEach { fmt ->
                    ElevatedFilterChip(
                        selected = selectedFormat == fmt,
                        onClick = { selectedFormat = fmt },
                        label = { Text(fmt) },
                        colors = FilterChipDefaults.elevatedFilterChipColors(
                            selectedContainerColor = ElectricViolet,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            AdjustmentSlider(
                title = "Compression Quality",
                value = qualityPercent,
                valueRange = 10f..100f,
                onValueChange = { qualityPercent = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Photo Button
            GradientButton(
                text = Strings.get("save_to_device", language),
                onClick = {
                    if (isSaving) return@GradientButton
                    isSaving = true
                    scope.launch {
                        val format = when (selectedFormat) {
                            "PNG" -> Bitmap.CompressFormat.PNG
                            "WEBP" -> Bitmap.CompressFormat.WEBP
                            else -> Bitmap.CompressFormat.JPEG
                        }
                        val file = editorViewModel.exportAndSave(
                            format = format,
                            quality = qualityPercent.roundToInt(),
                            title = "MI_Photo_${System.currentTimeMillis()}"
                        )
                        savedFile = file
                        isSaving = false
                        if (file != null) {
                            onPhotoSaved(file)
                        }
                    }
                },
                icon = {
                    Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                },
                testTag = "save_photo_action_button"
            )

            // Saved confirmation banner & Share options
            if (savedFile != null) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = NeonCyan.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonCyan)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = Strings.get("save_success", language),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { savedFile?.let { onShareClick(it) } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = ElectricViolet)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Strings.get("share_photo", language),
                        color = ElectricViolet,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
