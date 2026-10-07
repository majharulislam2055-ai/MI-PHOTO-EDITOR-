package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Portrait
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Rotate90DegreesCw
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.components.BeforeAfterSlider
import com.example.ui.screens.tools.BackgroundEditorPanel
import com.example.ui.screens.tools.CropTransformPanel
import com.example.ui.screens.tools.DrawBrushPanel
import com.example.ui.screens.tools.EffectsEditorPanel
import com.example.ui.screens.tools.EnhanceAndRestorePanel
import com.example.ui.screens.tools.FaceEditorPanel
import com.example.ui.screens.tools.FilterEditorPanel
import com.example.ui.screens.tools.LightingEditorPanel
import com.example.ui.screens.tools.ObjectRemovalPanel
import com.example.ui.screens.tools.PortraitAndProPanel
import com.example.ui.screens.tools.TextAndStickerPanel
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonCyan
import com.example.viewmodel.EditorTab
import com.example.viewmodel.EditorViewModel

data class TabInfo(
    val tab: EditorTab,
    val icon: ImageVector,
    val labelEn: String,
    val labelBn: String
)

@Composable
fun EditorScreen(
    editorViewModel: EditorViewModel,
    language: AppLanguage,
    onBack: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val originalBitmap by editorViewModel.originalBitmap.collectAsState()
    val displayedBitmap by editorViewModel.displayedBitmap.collectAsState()
    val editState by editorViewModel.editState.collectAsState()
    val selectedTab by editorViewModel.selectedTab.collectAsState()
    val canUndo by editorViewModel.canUndo.collectAsState()
    val canRedo by editorViewModel.canRedo.collectAsState()
    val isProcessing by editorViewModel.isProcessing.collectAsState()
    val splitPos by editorViewModel.splitPosition.collectAsState()

    val tabs = listOf(
        TabInfo(EditorTab.ENHANCE, Icons.Default.AutoAwesome, "AI Enhance", "AI Enhance"),
        TabInfo(EditorTab.FACE, Icons.Default.Face, "Face", "Face"),
        TabInfo(EditorTab.BACKGROUND, Icons.Default.Landscape, "Background", "Background"),
        TabInfo(EditorTab.LIGHTING, Icons.Default.LightMode, "Lighting", "Lighting"),
        TabInfo(EditorTab.FILTERS, Icons.Default.Filter, "Filters", "Filters"),
        TabInfo(EditorTab.EFFECTS, Icons.Default.Palette, "Effects", "Effects"),
        TabInfo(EditorTab.REMOVE_OBJECT, Icons.Default.AutoFixHigh, "Remove Obj", "Remove Obj"),
        TabInfo(EditorTab.RESTORE_ENHANCE, Icons.Default.History, "Enhancer", "Enhancer"),
        TabInfo(EditorTab.CROP_TRANSFORM, Icons.Default.Crop, "Crop", "Crop"),
        TabInfo(EditorTab.TEXT_STICKER, Icons.Default.TextFields, "Text", "Text"),
        TabInfo(EditorTab.DRAW_BRUSH, Icons.Default.Brush, "Draw", "Draw"),
        TabInfo(EditorTab.PORTRAIT, Icons.Default.Portrait, "Portrait", "Portrait"),
        TabInfo(EditorTab.PROFESSIONAL, Icons.Default.Badge, "Professional", "Professional")
    )

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("photo_editor_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // 1. Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("editor_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = "MI PHOTO EDITOR",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { editorViewModel.undo() },
                        enabled = canUndo,
                        modifier = Modifier.testTag("editor_undo_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Undo,
                            contentDescription = "Undo",
                            tint = if (canUndo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                        )
                    }

                    IconButton(
                        onClick = { editorViewModel.redo() },
                        enabled = canRedo,
                        modifier = Modifier.testTag("editor_redo_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Redo,
                            contentDescription = "Redo",
                            tint = if (canRedo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                        )
                    }

                    IconButton(
                        onClick = { editorViewModel.reset() },
                        modifier = Modifier.testTag("editor_reset_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Reset",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Button(
                        onClick = onSaveClick,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                        modifier = Modifier.padding(start = 4.dp).testTag("editor_save_button")
                    ) {
                        Text(
                            text = Strings.get("save_btn", language),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // 2. Interactive Photo Canvas (Before/After split slider)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                BeforeAfterSlider(
                    beforeBitmap = originalBitmap,
                    afterBitmap = displayedBitmap,
                    modifier = Modifier.fillMaxSize(),
                    initialSplit = splitPos,
                    beforeLabel = Strings.get("original", language),
                    afterLabel = Strings.get("edited", language),
                    onSplitChanged = { editorViewModel.setSplitPosition(it) }
                )

                // Quick Floating Controls Overlay (Zoom, Rotate)
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black.copy(alpha = 0.65f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { editorViewModel.zoomIn() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.ZoomIn, contentDescription = "Zoom in", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = { editorViewModel.zoomOut() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.ZoomOut, contentDescription = "Zoom out", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = { editorViewModel.rotate90() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Rotate90DegreesCw, contentDescription = "Rotate", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }

                // Processing Spinner Overlay
                if (isProcessing) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.75f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NeonCyan, strokeWidth = 3.dp, modifier = Modifier.size(32.dp))
                    }
                }
            }

            // 3. Tool Tabs Horizontal Scroller
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(tabs) { tabInfo ->
                    val isSelected = selectedTab == tabInfo.tab
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) ElectricViolet.copy(alpha = 0.15f) else Color.Transparent)
                            .clickable { editorViewModel.selectTab(tabInfo.tab) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = tabInfo.icon,
                            contentDescription = tabInfo.labelEn,
                            tint = if (isSelected) ElectricViolet else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (language == AppLanguage.BN) tabInfo.labelBn else tabInfo.labelEn,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) ElectricViolet else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 4. Tool Controls Panel Drawer (Bottom section)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                when (selectedTab) {
                    EditorTab.ENHANCE -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = Strings.get("ai_auto_btn", language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = Strings.get("ai_auto_desc", language),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { editorViewModel.triggerAiAutoEnhance() },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet)
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Re-run AI Auto Enhance", color = Color.White)
                            }
                        }
                    }
                    EditorTab.FACE -> {
                        FaceEditorPanel(
                            face = editState.face,
                            language = language,
                            onFaceChange = { editorViewModel.updateFaceAdjustments(it) }
                        )
                    }
                    EditorTab.BACKGROUND -> {
                        BackgroundEditorPanel(
                            bg = editState.background,
                            language = language,
                            onBgChange = { editorViewModel.updateBackgroundAdjustments(it) },
                            onPickCustomBg = { /* Trigger custom bg */ }
                        )
                    }
                    EditorTab.LIGHTING -> {
                        LightingEditorPanel(
                            lighting = editState.lighting,
                            language = language,
                            onLightingChange = { editorViewModel.updateLightingAdjustments(it) }
                        )
                    }
                    EditorTab.FILTERS -> {
                        FilterEditorPanel(
                            filter = editState.filter,
                            language = language,
                            onFilterChange = { id, intensity -> editorViewModel.updateFilter(id, intensity) }
                        )
                    }
                    EditorTab.EFFECTS -> {
                        EffectsEditorPanel(
                            effect = editState.effect,
                            language = language,
                            onEffectChange = { id, intensity -> editorViewModel.updateEffect(id, intensity) }
                        )
                    }
                    EditorTab.REMOVE_OBJECT -> {
                        ObjectRemovalPanel(
                            language = language,
                            onRemoveObject = { editorViewModel.removeObject() },
                            onClearMask = { }
                        )
                    }
                    EditorTab.RESTORE_ENHANCE -> {
                        EnhanceAndRestorePanel(
                            language = language,
                            onRestoreOldPhoto = { editorViewModel.restoreOldPhoto(it) },
                            onUpscale = { }
                        )
                    }
                    EditorTab.CROP_TRANSFORM -> {
                        CropTransformPanel(
                            crop = editState.cropTransform,
                            language = language,
                            onRotate = { editorViewModel.rotate90() },
                            onFlipH = { editorViewModel.flipHorizontal() },
                            onFlipV = { editorViewModel.flipVertical() },
                            onRatioChange = { editorViewModel.setAspectRatio(it) }
                        )
                    }
                    EditorTab.TEXT_STICKER -> {
                        TextAndStickerPanel(
                            language = language,
                            onAddText = { editorViewModel.addTextOverlay(it) },
                            onAddSticker = { editorViewModel.addSticker(it) }
                        )
                    }
                    EditorTab.DRAW_BRUSH -> {
                        DrawBrushPanel(
                            language = language,
                            onColorSelected = { },
                            onSizeSelected = { }
                        )
                    }
                    EditorTab.PORTRAIT, EditorTab.PROFESSIONAL, EditorTab.PRESETS -> {
                        PortraitAndProPanel(
                            language = language,
                            onOneTapPortrait = {
                                editorViewModel.triggerAiAutoEnhance()
                            },
                            onApplyProStyle = { style ->
                                editorViewModel.updateBackgroundAdjustments {
                                    it.copy(solidColor = style.targetBg, removeBackground = false)
                                }
                            },
                            onApplyQuickPreset = { preset ->
                                editorViewModel.updateFilter("portrait_pro", 85f)
                            }
                        )
                    }
                }
            }
        }
    }
}
