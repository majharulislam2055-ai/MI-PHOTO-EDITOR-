package com.example.ui.screens.tools

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.FaceAdjustments
import com.example.model.Strings
import com.example.ui.components.AdjustmentSlider

@Composable
fun FaceEditorPanel(
    face: FaceAdjustments,
    language: AppLanguage,
    onFaceChange: ((FaceAdjustments) -> FaceAdjustments) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        AdjustmentSlider(
            title = Strings.get("skin_smooth", language),
            value = face.skinSmooth,
            valueRange = 0f..100f,
            onValueChange = { onFaceChange { f -> f.copy(skinSmooth = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("skin_tone", language),
            value = face.skinTone,
            valueRange = -50f..50f,
            onValueChange = { onFaceChange { f -> f.copy(skinTone = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("natural_beauty", language),
            value = face.naturalBeauty,
            valueRange = 0f..100f,
            onValueChange = { onFaceChange { f -> f.copy(naturalBeauty = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("face_brightness", language),
            value = face.faceBrightness,
            valueRange = -50f..50f,
            onValueChange = { onFaceChange { f -> f.copy(faceBrightness = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("face_contrast", language),
            value = face.faceContrast,
            valueRange = -50f..50f,
            onValueChange = { onFaceChange { f -> f.copy(faceContrast = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("eye_brightness", language),
            value = face.eyeBrightness,
            valueRange = 0f..100f,
            onValueChange = { onFaceChange { f -> f.copy(eyeBrightness = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("eye_detail", language),
            value = face.eyeDetail,
            valueRange = 0f..100f,
            onValueChange = { onFaceChange { f -> f.copy(eyeDetail = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("teeth_whitening", language),
            value = face.teethWhitening,
            valueRange = 0f..100f,
            onValueChange = { onFaceChange { f -> f.copy(teethWhitening = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("blemish_removal", language),
            value = face.blemishRemoval,
            valueRange = 0f..100f,
            onValueChange = { onFaceChange { f -> f.copy(blemishRemoval = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("acne_reduction", language),
            value = face.acneReduction,
            valueRange = 0f..100f,
            onValueChange = { onFaceChange { f -> f.copy(acneReduction = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("dark_circle", language),
            value = face.darkCircleReduction,
            valueRange = 0f..100f,
            onValueChange = { onFaceChange { f -> f.copy(darkCircleReduction = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("face_light", language),
            value = face.faceLight,
            valueRange = -50f..50f,
            onValueChange = { onFaceChange { f -> f.copy(faceLight = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("face_shadow", language),
            value = face.faceShadow,
            valueRange = -50f..50f,
            onValueChange = { onFaceChange { f -> f.copy(faceShadow = it) } }
        )

        AdjustmentSlider(
            title = Strings.get("face_detail", language),
            value = face.faceDetail,
            valueRange = 0f..100f,
            onValueChange = { onFaceChange { f -> f.copy(faceDetail = it) } }
        )
    }
}
