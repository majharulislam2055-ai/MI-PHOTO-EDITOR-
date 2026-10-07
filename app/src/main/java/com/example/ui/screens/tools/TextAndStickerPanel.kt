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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.model.StickerOverlayItem
import com.example.model.TextOverlayItem
import com.example.ui.components.GradientButton
import com.example.ui.theme.ElectricViolet

@Composable
fun TextAndStickerPanel(
    language: AppLanguage,
    onAddText: (TextOverlayItem) -> Unit,
    onAddSticker: (StickerOverlayItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var textInput by remember { mutableStateOf("MI Style") }
    var textColor by remember { mutableStateOf(Color.White) }

    val colors = listOf(Color.White, Color.Black, Color(0xFFFBBF24), Color(0xFFEC4899), Color(0xFF06B6D4), Color(0xFF7C3AED))
    val emojis = listOf("✨", "🌟", "👑", "🔥", "❤️", "💎", "🌸", "⚡", "📸", "🎨", "🕶️", "🦋")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Add Custom Text",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                placeholder = { Text("Enter text...") }
            )

            GradientButton(
                text = "Add",
                onClick = {
                    if (textInput.isNotBlank()) {
                        onAddText(
                            TextOverlayItem(
                                text = textInput,
                                color = textColor,
                                fontSize = 32f,
                                isBold = true
                            )
                        )
                    }
                },
                modifier = Modifier.size(width = 80.dp, height = 52.dp),
                height = 52.dp
            )
        }

        // Color selector
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            items(colors) { c ->
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(c)
                        .border(
                            width = if (textColor == c) 2.5.dp else 1.dp,
                            color = if (textColor == c) ElectricViolet else Color.Gray,
                            shape = CircleShape
                        )
                        .clickable { textColor = c }
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "AI Stickers & Emojis",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(emojis) { emoji ->
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            onAddSticker(
                                StickerOverlayItem(
                                    emojiOrDrawable = emoji,
                                    size = 56f
                                )
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, fontSize = 24.sp)
                }
            }
        }
    }
}
