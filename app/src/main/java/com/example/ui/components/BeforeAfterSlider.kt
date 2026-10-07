package com.example.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricViolet
import kotlin.math.roundToInt

@Composable
fun BeforeAfterSlider(
    beforeBitmap: Bitmap?,
    afterBitmap: Bitmap?,
    modifier: Modifier = Modifier,
    initialSplit: Float = 0.5f,
    beforeLabel: String = "Original",
    afterLabel: String = "AI Edited",
    onSplitChanged: (Float) -> Unit = {}
) {
    if (beforeBitmap == null && afterBitmap == null) return

    var split by remember { mutableFloatStateOf(initialSplit) }
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            .testTag("before_after_slider")
    ) {
        val containerWidth = constraints.maxWidth.toFloat()
        val containerHeight = constraints.maxHeight.toFloat()

        val beforeImage = remember(beforeBitmap) { beforeBitmap?.asImageBitmap() }
        val afterImage = remember(afterBitmap) { afterBitmap?.asImageBitmap() }

        // Canvas drawing both bitmaps with split clipping
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        val newSplit = (change.position.x / containerWidth).coerceIn(0.05f, 0.95f)
                        split = newSplit
                        onSplitChanged(newSplit)
                    }
                }
        ) {
            val splitPx = containerWidth * split

            // 1. Draw "Before" image on the left side
            if (beforeImage != null) {
                clipRect(left = 0f, top = 0f, right = splitPx, bottom = containerHeight) {
                    drawImage(
                        image = beforeImage,
                        dstSize = androidx.compose.ui.unit.IntSize(
                            containerWidth.roundToInt(),
                            containerHeight.roundToInt()
                        )
                    )
                }
            }

            // 2. Draw "After" image on the right side
            if (afterImage != null) {
                clipRect(left = splitPx, top = 0f, right = containerWidth, bottom = containerHeight) {
                    drawImage(
                        image = afterImage,
                        dstSize = androidx.compose.ui.unit.IntSize(
                            containerWidth.roundToInt(),
                            containerHeight.roundToInt()
                        )
                    )
                }
            }

            // 3. Draw vertical divider line
            drawLine(
                color = Color.White,
                start = Offset(splitPx, 0f),
                end = Offset(splitPx, containerHeight),
                strokeWidth = 3.dp.toPx()
            )
        }

        // Circular drag handle in center of divider
        val handleRadiusPx = with(density) { 18.dp.toPx() }
        val handleX = (containerWidth * split - handleRadiusPx).roundToInt()
        val handleY = (containerHeight / 2f - handleRadiusPx).roundToInt()

        Box(
            modifier = Modifier
                .offset { IntOffset(handleX, handleY) }
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val newSplit = ((containerWidth * split + dragAmount.x) / containerWidth)
                            .coerceIn(0.05f, 0.95f)
                        split = newSplit
                        onSplitChanged(newSplit)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CompareArrows,
                contentDescription = "Slide to compare",
                tint = ElectricViolet,
                modifier = Modifier.size(22.dp)
            )
        }

        // Badges: "Original" & "AI Edited"
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(alpha = 0.65f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = beforeLabel,
                color = Color.White,
                fontSize = 12.sp,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(ElectricViolet.copy(alpha = 0.85f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = afterLabel,
                color = Color.White,
                fontSize = 12.sp,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
