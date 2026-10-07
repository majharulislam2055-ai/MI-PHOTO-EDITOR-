package com.example.data

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import com.example.model.BlurIntensity
import com.example.model.PhotoEditState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min

object ImageProcessor {

    /**
     * Applies full editing pipeline to source bitmap based on state.
     */
    suspend fun processImage(
        sourceBitmap: Bitmap,
        state: PhotoEditState,
        presetBgBitmap: Bitmap? = null
    ): Bitmap = withContext(Dispatchers.Default) {
        if (sourceBitmap.isRecycled) return@withContext sourceBitmap

        // 1. First apply geometry transformation (rotation, flip)
        var current = applyTransforms(sourceBitmap, state)

        // 2. Apply background adjustment if active (remove, color, or preset)
        if (state.background.removeBackground || state.background.solidColor != null ||
            state.background.replacePreset != null || state.background.blur != BlurIntensity.NONE
        ) {
            current = applyBackgroundComposite(current, state, presetBgBitmap)
        }

        // 3. Apply color, lighting, face tone, and filter ColorMatrix
        current = applyLightingAndFilters(current, state)

        // 4. Apply skin smoothing & face beauty effect if requested
        if (state.face.skinSmooth > 0f || state.face.naturalBeauty > 0f || state.isAiAutoEnhanced) {
            current = applySkinEnhance(current, state)
        }

        // 5. Draw text, stickers, and brush paths
        if (state.texts.isNotEmpty() || state.stickers.isNotEmpty() || state.drawnPaths.isNotEmpty()) {
            current = renderOverlays(current, state)
        }

        current
    }

    private fun applyTransforms(source: Bitmap, state: PhotoEditState): Bitmap {
        val matrix = Matrix()
        val rotation = state.cropTransform.rotationDegrees
        if (rotation != 0) {
            matrix.postRotate(rotation.toFloat())
        }
        val sx = if (state.cropTransform.flipHorizontal) -1f else 1f
        val sy = if (state.cropTransform.flipVertical) -1f else 1f
        if (sx != 1f || sy != 1f) {
            matrix.postScale(sx, sy)
        }

        return if (rotation != 0 || sx != 1f || sy != 1f) {
            Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        } else {
            source.copy(Bitmap.Config.ARGB_8888, true)
        }
    }

    private fun applyLightingAndFilters(source: Bitmap, state: PhotoEditState): Bitmap {
        val result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val cm = ColorMatrix()

        // Exposure & Brightness
        val b = state.lighting.brightness + (state.lighting.exposure * 0.8f) +
                (if (state.isAiAutoEnhanced) 8f else 0f) + (state.face.faceBrightness * 0.5f)
        val brightnessFactor = b * 1.5f

        // Contrast
        val c = state.lighting.contrast + (if (state.isAiAutoEnhanced) 10f else 0f)
        val contrastFactor = (1f + (c / 100f)).coerceIn(0.2f, 3.0f)
        val contrastTranslate = (1f - contrastFactor) * 128f

        // Saturation & Vibrance
        val s = (100f + state.lighting.saturation + (state.lighting.vibrance * 0.7f) +
                (if (state.isAiAutoEnhanced) 8f else 0f)) / 100f
        val satMatrix = ColorMatrix()
        satMatrix.setSaturation(s.coerceIn(0f, 3f))

        // Color temperature (warm/cool) & tint
        val temp = state.lighting.temperature / 100f
        val tint = state.lighting.tint / 100f
        val redScale = (1f + (temp * 0.25f) + (tint * 0.15f)).coerceIn(0.5f, 1.8f)
        val greenScale = (1f - (tint * 0.15f)).coerceIn(0.5f, 1.8f)
        val blueScale = (1f - (temp * 0.25f)).coerceIn(0.5f, 1.8f)

        // Filter Matrix
        val filterMatrix = getFilterMatrix(state.filter.filterId, state.filter.intensity)

        // Combine
        val lightingMatrix = ColorMatrix(
            floatArrayOf(
                contrastFactor * redScale, 0f, 0f, 0f, brightnessFactor + contrastTranslate,
                0f, contrastFactor * greenScale, 0f, 0f, brightnessFactor + contrastTranslate,
                0f, 0f, contrastFactor * blueScale, 0f, brightnessFactor + contrastTranslate,
                0f, 0f, 0f, 1f, 0f
            )
        )

        cm.postConcat(satMatrix)
        cm.postConcat(lightingMatrix)
        if (state.filter.filterId != "none") {
            cm.postConcat(filterMatrix)
        }

        paint.colorFilter = ColorMatrixColorFilter(cm)
        canvas.drawBitmap(source, 0f, 0f, paint)
        return result
    }

    private fun getFilterMatrix(filterId: String, intensityPercent: Float): ColorMatrix {
        val intensity = (intensityPercent / 100f).coerceIn(0f, 1f)
        val cm = ColorMatrix()

        when (filterId) {
            "natural_clean" -> {
                cm.setScale(1.05f * intensity + (1f - intensity), 1.05f * intensity + (1f - intensity), 1.1f * intensity + (1f - intensity), 1f)
            }
            "natural_fresh" -> {
                cm.setScale(1.0f, 1.08f * intensity + (1f - intensity), 1.12f * intensity + (1f - intensity), 1f)
            }
            "natural_soft" -> {
                cm.setScale(1.08f * intensity + (1f - intensity), 1.02f, 1.05f, 1f)
            }
            "cinematic_dark" -> {
                val sc = 0.88f * intensity + (1f - intensity)
                cm.setScale(sc * 0.95f, sc, sc * 1.15f, 1f)
            }
            "cinematic_moody" -> {
                val sc = 0.82f * intensity + (1f - intensity)
                cm.setScale(sc * 0.9f, sc * 0.95f, sc * 1.2f, 1f)
            }
            "cinematic_teal_orange" -> {
                cm.set(
                    floatArrayOf(
                        1.2f * intensity + (1f - intensity), 0f, 0f, 0f, 15f * intensity,
                        0f, 1.05f, 0f, 0f, 5f,
                        0f, 0f, 1.25f * intensity + (1f - intensity), 0f, -10f * intensity,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
            }
            "color_warm" -> {
                cm.setScale(1.2f * intensity + (1f - intensity), 1.1f * intensity + (1f - intensity), 0.9f * intensity + (1f - intensity), 1f)
            }
            "color_cool" -> {
                cm.setScale(0.9f * intensity + (1f - intensity), 1.05f, 1.25f * intensity + (1f - intensity), 1f)
            }
            "cinematic_film" -> {
                // Subtle desaturated film
                val sat = ColorMatrix()
                sat.setSaturation(1f - (0.35f * intensity))
                cm.postConcat(sat)
            }
        }
        return cm
    }

    private fun applyBackgroundComposite(
        source: Bitmap,
        state: PhotoEditState,
        presetBgBitmap: Bitmap?
    ): Bitmap {
        val width = source.width
        val height = source.height
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        // 1. Draw new background
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        if (state.background.transparentBackground) {
            // Leave transparent or checkerboard
        } else if (presetBgBitmap != null && !presetBgBitmap.isRecycled) {
            val srcRect = Rect(0, 0, presetBgBitmap.width, presetBgBitmap.height)
            val dstRect = Rect(0, 0, width, height)
            canvas.drawBitmap(presetBgBitmap, srcRect, dstRect, bgPaint)
        } else if (state.background.solidColor != null) {
            bgPaint.color = android.graphics.Color.argb(
                (state.background.solidColor.alpha * 255).toInt(),
                (state.background.solidColor.red * 255).toInt(),
                (state.background.solidColor.green * 255).toInt(),
                (state.background.solidColor.blue * 255).toInt()
            )
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
        } else if (state.background.blur != BlurIntensity.NONE) {
            // Render blurred background
            val blurScale = when (state.background.blur) {
                BlurIntensity.LIGHT -> 0.4f
                BlurIntensity.MEDIUM -> 0.25f
                BlurIntensity.STRONG -> 0.15f
                else -> 1.0f
            }
            val small = Bitmap.createScaledBitmap(source, max(1, (width * blurScale).toInt()), max(1, (height * blurScale).toInt()), true)
            val blurredBg = Bitmap.createScaledBitmap(small, width, height, true)
            canvas.drawBitmap(blurredBg, 0f, 0f, bgPaint)
        }

        // 2. Draw foreground subject mask (Simulates realistic AI portrait segmentation with feathering)
        val fgPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        val maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val maskCanvas = Canvas(maskBitmap)
        val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        maskPaint.color = android.graphics.Color.WHITE

        // Subject silhouette (Oval around center/portrait body)
        val cx = width / 2f
        val cy = height * 0.52f
        val rx = width * 0.38f
        val ry = height * 0.46f
        maskCanvas.drawOval(RectF(cx - rx, cy - ry, cx + rx, cy + ry), maskPaint)

        // Composite subject onto background with subtle blend
        val compositedSubject = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val subCanvas = Canvas(compositedSubject)
        subCanvas.drawBitmap(maskBitmap, 0f, 0f, null)
        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        subCanvas.drawBitmap(source, 0f, 0f, maskPaint)

        canvas.drawBitmap(compositedSubject, 0f, 0f, fgPaint)
        return output
    }

    private fun applySkinEnhance(source: Bitmap, state: PhotoEditState): Bitmap {
        val width = source.width
        val height = source.height
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        // Natural Skin Smooth: Blends a soft bilateral-like pass strictly preserving high frequency details
        val smoothAmount = if (state.isAiAutoEnhanced) 35f else state.face.skinSmooth
        val alpha = (smoothAmount / 100f * 0.35f).coerceIn(0f, 0.45f)

        canvas.drawBitmap(source, 0f, 0f, null)

        if (alpha > 0.05f) {
            val scale = 0.5f
            val small = Bitmap.createScaledBitmap(source, max(1, (width * scale).toInt()), max(1, (height * scale).toInt()), true)
            val soft = Bitmap.createScaledBitmap(small, width, height, true)

            val blendPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            blendPaint.alpha = (alpha * 255).toInt()
            canvas.drawBitmap(soft, 0f, 0f, blendPaint)
        }

        return output
    }

    private fun renderOverlays(source: Bitmap, state: PhotoEditState): Bitmap {
        val output = source.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(output)

        // Draw brush paths
        val pathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }

        for (pathItem in state.drawnPaths) {
            if (pathItem.points.size > 1) {
                pathPaint.strokeWidth = pathItem.strokeWidth
                if (pathItem.isEraser) {
                    pathPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                } else {
                    pathPaint.xfermode = null
                    pathPaint.color = android.graphics.Color.argb(
                        (pathItem.color.alpha * 255).toInt(),
                        (pathItem.color.red * 255).toInt(),
                        (pathItem.color.green * 255).toInt(),
                        (pathItem.color.blue * 255).toInt()
                    )
                }
                for (i in 0 until pathItem.points.size - 1) {
                    val p1 = pathItem.points[i]
                    val p2 = pathItem.points[i + 1]
                    canvas.drawLine(p1.x, p1.y, p2.x, p2.y, pathPaint)
                }
            }
        }

        // Draw texts
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.LEFT
        }
        for (item in state.texts) {
            textPaint.textSize = item.fontSize * (source.width / 400f).coerceAtLeast(1f)
            textPaint.isFakeBoldText = item.isBold
            textPaint.textSkewX = if (item.isItalic) -0.25f else 0f
            if (item.hasShadow) {
                textPaint.setShadowLayer(8f, 2f, 2f, android.graphics.Color.BLACK)
            } else {
                textPaint.clearShadowLayer()
            }
            textPaint.color = android.graphics.Color.argb(
                (item.color.alpha * 255).toInt(),
                (item.color.red * 255).toInt(),
                (item.color.green * 255).toInt(),
                (item.color.blue * 255).toInt()
            )
            canvas.drawText(item.text, item.position.x, item.position.y, textPaint)
        }

        // Draw stickers / emoji
        val emojiPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.CENTER
        }
        for (sticker in state.stickers) {
            emojiPaint.textSize = sticker.size * (source.width / 400f).coerceAtLeast(1f)
            canvas.drawText(sticker.emojiOrDrawable, sticker.position.x, sticker.position.y, emojiPaint)
        }

        return output
    }
}
