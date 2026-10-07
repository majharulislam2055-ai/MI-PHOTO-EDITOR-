package com.example.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class FaceAdjustments(
    val skinSmooth: Float = 0f,         // 0 to 100
    val skinTone: Float = 0f,           // -50 to 50
    val faceBrightness: Float = 0f,     // -50 to 50
    val faceContrast: Float = 0f,       // -50 to 50
    val eyeBrightness: Float = 0f,      // 0 to 100
    val eyeDetail: Float = 0f,          // 0 to 100
    val teethWhitening: Float = 0f,     // 0 to 100
    val blemishRemoval: Float = 0f,     // 0 to 100
    val acneReduction: Float = 0f,      // 0 to 100
    val darkCircleReduction: Float = 0f,// 0 to 100
    val faceLight: Float = 0f,          // -50 to 50
    val faceShadow: Float = 0f,         // -50 to 50
    val naturalBeauty: Float = 0f,      // 0 to 100
    val faceDetail: Float = 0f          // 0 to 100
)

data class LightingAdjustments(
    val brightness: Float = 0f,    // -100 to 100
    val exposure: Float = 0f,      // -100 to 100
    val contrast: Float = 0f,      // -100 to 100
    val highlights: Float = 0f,    // -100 to 100
    val shadows: Float = 0f,       // -100 to 100
    val temperature: Float = 0f,   // -100 to 100 (warm/cool)
    val tint: Float = 0f,          // -100 to 100 (green/magenta)
    val vibrance: Float = 0f,      // -100 to 100
    val saturation: Float = 0f,    // -100 to 100
    val sharpness: Float = 0f,     // 0 to 100
    val clarity: Float = 0f        // 0 to 100
)

enum class BlurIntensity {
    NONE, LIGHT, MEDIUM, STRONG
}

data class BackgroundAdjustments(
    val blur: BlurIntensity = BlurIntensity.NONE,
    val solidColor: Color? = null,
    val replacePreset: String? = null,
    val customBgPath: String? = null,
    val removeBackground: Boolean = false,
    val transparentBackground: Boolean = false
)

data class FilterAdjustment(
    val filterId: String = "none",
    val intensity: Float = 100f // 0 to 100
)

data class EffectAdjustment(
    val effectId: String = "none",
    val intensity: Float = 80f
)

data class CropTransform(
    val rotationDegrees: Int = 0, // 0, 90, 180, 270
    val flipHorizontal: Boolean = false,
    val flipVertical: Boolean = false,
    val aspectRatio: String = "Free" // Free, 1:1, 4:5, 3:4, 16:9, 9:16
)

data class TextOverlayItem(
    val id: String = System.currentTimeMillis().toString(),
    val text: String = "MI PHOTO",
    val position: Offset = Offset(200f, 300f),
    val color: Color = Color.White,
    val fontSize: Float = 28f,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val hasShadow: Boolean = true
)

data class StickerOverlayItem(
    val id: String = System.currentTimeMillis().toString(),
    val emojiOrDrawable: String = "✨",
    val position: Offset = Offset(200f, 200f),
    val size: Float = 60f
)

data class DrawPathItem(
    val points: List<Offset> = emptyList(),
    val color: Color = Color.Magenta,
    val strokeWidth: Float = 12f,
    val isEraser: Boolean = false
)

data class PhotoEditState(
    val isAiAutoEnhanced: Boolean = false,
    val face: FaceAdjustments = FaceAdjustments(),
    val lighting: LightingAdjustments = LightingAdjustments(),
    val background: BackgroundAdjustments = BackgroundAdjustments(),
    val filter: FilterAdjustment = FilterAdjustment(),
    val effect: EffectAdjustment = EffectAdjustment(),
    val cropTransform: CropTransform = CropTransform(),
    val texts: List<TextOverlayItem> = emptyList(),
    val stickers: List<StickerOverlayItem> = emptyList(),
    val drawnPaths: List<DrawPathItem> = emptyList(),
    val objectRemovalPoints: List<Offset> = emptyList(),
    val upscaleFactor: Int = 1, // 1x, 2x, 4x
    val isOldPhotoRestored: Boolean = false,
    val isBwToColor: Boolean = false
)
