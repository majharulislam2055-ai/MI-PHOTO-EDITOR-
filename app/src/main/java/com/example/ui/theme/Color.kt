package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Gradient & Brand Colors
val ElectricPurple = Color(0xFF7C3AED)
val ElectricViolet = Color(0xFF8B5CF6)
val NeonCyan = Color(0xFF06B6D4)
val DeepIndigo = Color(0xFF4F46E5)
val SoftPink = Color(0xFFEC4899)
val GoldAccent = Color(0xFFF59E0B)

// Dark Theme Surfaces
val DarkBg = Color(0xFF0B0D14)
val DarkSurface = Color(0xFF131622)
val DarkSurfaceVariant = Color(0xFF1C2030)
val DarkCard = Color(0xFF181C2A)
val DarkBorder = Color(0xFF282E45)

// Light Theme Surfaces
val LightBg = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF1F5F9)
val LightCard = Color(0xFFFFFFFF)
val LightBorder = Color(0xFFE2E8F0)

// Text Colors
val TextPrimaryDark = Color(0xFFF8FAFC)
val TextSecondaryDark = Color(0xFF94A3B8)
val TextPrimaryLight = Color(0xFF0F172A)
val TextSecondaryLight = Color(0xFF64748B)

// Gradient Brushes
val BrandGradient = Brush.horizontalGradient(
    colors = listOf(ElectricPurple, NeonCyan)
)

val HeroGradient = Brush.linearGradient(
    colors = listOf(ElectricPurple, DeepIndigo, NeonCyan)
)

val GlowGradient = Brush.radialGradient(
    colors = listOf(ElectricViolet.copy(alpha = 0.4f), Color.Transparent)
)

val AccentGradient = Brush.linearGradient(
    colors = listOf(ElectricViolet, SoftPink)
)
