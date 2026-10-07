package com.example.model

import androidx.compose.ui.graphics.Color

data class FilterItem(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val category: String,
    val previewColor: Color
)

data class LightingPresetItem(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val brightness: Float,
    val exposure: Float,
    val contrast: Float,
    val warmth: Float,
    val saturation: Float
)

data class BackgroundPresetItem(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val previewGradient: List<Color>,
    val drawableResName: String? = null
)

data class ProfessionalStyleItem(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val description: String,
    val targetBg: Color
)

object PresetRepository {

    val filters = listOf(
        // Natural
        FilterItem("natural_clean", "Clean", "ক্লিন", "Natural", Color(0xFFE2E8F0)),
        FilterItem("natural_fresh", "Fresh", "ফ্রেশ", "Natural", Color(0xFFBAE6FD)),
        FilterItem("natural_soft", "Soft", "সফট", "Natural", Color(0xFFFBCFE8)),
        FilterItem("natural_glow", "Natural Glow", "ন্যাচারাল গ্লো", "Natural", Color(0xFFFEF08A)),

        // Portrait
        FilterItem("portrait_pro", "Professional", "প্রফেশনাল", "Portrait", Color(0xFFCBD5E1)),
        FilterItem("portrait_beauty", "Beauty", "বিউটি", "Portrait", Color(0xFFFDE047)),
        FilterItem("portrait_soft_skin", "Soft Skin", "সফট স্কিন", "Portrait", Color(0xFFFECDD3)),
        FilterItem("portrait_vogue", "Vogue", "ভোগ", "Portrait", Color(0xFFF472B6)),

        // Cinematic
        FilterItem("cinematic_dark", "Dark", "ডার্ক", "Cinematic", Color(0xFF334155)),
        FilterItem("cinematic_moody", "Moody", "মুডি", "Cinematic", Color(0xFF1E293B)),
        FilterItem("cinematic_film", "Film 35mm", "ফিল্ম ৩৫মিমি", "Cinematic", Color(0xFF78716C)),
        FilterItem("cinematic_teal_orange", "Teal & Orange", "টিল ও অরেঞ্জ", "Cinematic", Color(0xFF0D9488)),

        // Color
        FilterItem("color_warm", "Warm Gold", "ওয়ার্ম গোল্ড", "Color", Color(0xFFFBBF24)),
        FilterItem("color_cool", "Cool Cyan", "কুল সায়ান", "Color", Color(0xFF38BDF8)),
        FilterItem("color_vibrant", "Vibrant", "ভাইব্রেন্ট", "Color", Color(0xFFA855F7)),
        FilterItem("color_pastel", "Pastel Dream", "প্যাসটেল ড্রিম", "Color", Color(0xFFDDD6FE))
    )

    val lightingPresets = listOf(
        LightingPresetItem("natural", "Natural", "স্বাভাবিক", 5f, 5f, 10f, 0f, 5f),
        LightingPresetItem("studio", "Studio", "স্টুডিও", 18f, 15f, 22f, -5f, 10f),
        LightingPresetItem("warm", "Warm", "উষ্ণ", 10f, 10f, 15f, 28f, 15f),
        LightingPresetItem("cool", "Cool", "শীতল", 8f, 5f, 12f, -25f, 5f),
        LightingPresetItem("cinematic", "Cinematic", "সিনেমাটিক", -5f, -10f, 30f, 10f, 15f),
        LightingPresetItem("portrait", "Portrait", "পোর্ট্রেট", 15f, 12f, 15f, 8f, 12f),
        LightingPresetItem("sunset", "Sunset", "সূর্যাস্ত", 12f, 8f, 25f, 40f, 25f),
        LightingPresetItem("soft_light", "Soft Light", "সফট লাইট", 12f, 8f, -10f, 5f, 8f),
        LightingPresetItem("bright", "Bright", "উজ্জ্বল", 25f, 20f, 15f, 0f, 15f)
    )

    val bgPresets = listOf(
        BackgroundPresetItem("studio_luxury", "Studio Luxury", "লাক্সারি স্টুডিও", listOf(Color(0xFF232526), Color(0xFF414345)), "bg_studio_luxury"),
        BackgroundPresetItem("office", "Modern Office", "মডার্ন অফিস", listOf(Color(0xFF1F4037), Color(0xFF99F2C8))),
        BackgroundPresetItem("nature", "Lush Nature", "প্রকৃতি", listOf(Color(0xFF134E5E), Color(0xFF71B280))),
        BackgroundPresetItem("garden", "Botanical Garden", "গার্ডেন", listOf(Color(0xFF5A3F37), Color(0xFF2C7744))),
        BackgroundPresetItem("beach", "Tropical Beach", "সি বিচ", listOf(Color(0xFF00B4DB), Color(0xFF0083B0))),
        BackgroundPresetItem("city", "Urban City", "শহর", listOf(Color(0xFF0F2027), Color(0xFF203A43))),
        BackgroundPresetItem("sunset", "Golden Sunset", "সানসেট", listOf(Color(0xFFFF512F), Color(0xFFDD2476))),
        BackgroundPresetItem("luxury", "Royal Luxury", "রয়্যাল লাক্সারি", listOf(Color(0xFF141E30), Color(0xFF243B55))),
        BackgroundPresetItem("professional", "Corporate Blue", "কর্পোরেট ব্লু", listOf(Color(0xFF1E3C72), Color(0xFF2A5298))),
        BackgroundPresetItem("simple", "Clean Minimal", "সিম্পল মিনিমাল", listOf(Color(0xFFE0EAFC), Color(0xFFCFDEF3)))
    )

    val professionalStyles = listOf(
        ProfessionalStyleItem("passport", "Passport Style", "পাসপোর্ট স্টাইল", "Clean light background, centered portrait lighting", Color(0xFFF1F5F9)),
        ProfessionalStyleItem("cv_resume", "CV / Resume", "সিভি ও রিজিউমে", "Professional modern corporate aesthetic", Color(0xFFE2E8F0)),
        ProfessionalStyleItem("linkedin", "LinkedIn Style", "লিঙ্কডইন প্রোফাইল", "Executive confidence, subtle depth and bokeh", Color(0xFF0A66C2)),
        ProfessionalStyleItem("pro_portrait", "Professional Portrait", "প্রফেশনাল পোর্ট্রেট", "Studio grade key light with natural catchlights", Color(0xFF1E293B)),
        ProfessionalStyleItem("studio_headshot", "Studio Headshot", "স্টুডিও হেডশট", "Magazine cover balanced gradient backdrop", Color(0xFF18181B))
    )

    val effects = listOf(
        "Cinematic" to "সিনেমাটিক",
        "Portrait Glow" to "পোর্ট্রেট গ্লো",
        "Soft Glow" to "সফট গ্লো",
        "Dreamy" to "ড্রিমি",
        "HDR Ultra" to "এইচডিআর আল্ট্রা",
        "Professional" to "প্রফেশনাল",
        "Vintage 90s" to "ভিন্টেজ ৯০",
        "Classic Film" to "ক্লাসিক ফিল্ম",
        "Luxury Gold" to "লাক্সারি গোল্ড",
        "Dramatic" to "ড্রামাটিক"
    )

    val quickPresets = listOf(
        "Natural" to "ন্যাচারাল",
        "Beauty" to "বিউটি",
        "Professional" to "প্রফেশনাল",
        "Cinematic" to "সিনেমাটিক",
        "Studio" to "স্টুডিও",
        "Social Media" to "সোশ্যাল মিডিয়া",
        "Portrait" to "পোর্ট্রেট",
        "Vintage" to "ভিন্টেজ"
    )
}
