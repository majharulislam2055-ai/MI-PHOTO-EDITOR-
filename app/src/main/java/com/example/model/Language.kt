package com.example.model

enum class AppLanguage {
    BN, // Bengali (বাংলা)
    EN  // English
}

object Strings {
    private val bnMap = mapOf(
        "app_title" to "MI PHOTO EDITOR",
        "tagline" to "AI দিয়ে আপনার ছবিকে আরও সুন্দর ও প্রফেশনাল করুন",
        "tagline_short" to "AI দিয়ে ছবিকে দিন নতুন রূপ",
        "upload_hero" to "ছবি আপলোড করুন",
        "upload_sub" to "Gallery থেকে ছবি নির্বাচন করুন বা ক্যামেরা দিয়ে তুলুন",
        "pick_gallery" to "Gallery থেকে ছবি",
        "take_camera" to "ক্যামেরা দিয়ে ছবি",
        "sample_photo" to "স্যাম্পল ছবি দিয়ে ট্রাই করুন",
        "ai_auto_mode" to "AI Auto Mode",
        "ai_auto_desc" to "ছবি আপলোড করলেই AI নিজে থেকে ছবির সেরা Edit তৈরি করবে।",
        "ai_auto_btn" to "AI AUTO EDIT",
        "ai_auto_btn_sub" to "এক ক্লিকেই আপনার ছবিকে আরও সুন্দর করুন",
        "recent_edits" to "Recent Edits",
        "favorites" to "Favorites",
        "view_all" to "সব দেখুন",
        "no_recents" to "এখনও কোনো সাম্প্রতিক এডিট নেই",
        "no_recents_desc" to "একটি ছবি আপলোড করে এডিট শুরু করুন",
        "no_favs" to "কোনো প্রিয় ছবি সংরক্ষিত নেই",
        "quick_presets" to "জনপ্রিয় AI প্রিসেট",

        // Navigation
        "nav_home" to "Home",
        "nav_gallery" to "Gallery",
        "nav_edit" to "Edit",
        "nav_favorites" to "Favorites",
        "nav_profile" to "Profile",

        // AI Processing
        "analyzing_title" to "AI আপনার ছবিটি বিশ্লেষণ করছে...",
        "step_face" to "Face Detecting",
        "step_bg" to "Background Detecting",
        "step_light" to "Lighting Optimizing",
        "step_skin" to "Skin Enhancing",
        "step_color" to "Color Correcting",
        "step_final" to "Finalizing",
        "ready_msg" to "আপনার ছবি প্রস্তুত!",
        "natural_disclaimer" to "Natural ও বাস্তবসম্মত ফলাফল সংরক্ষিত",

        // Editor Top & Common
        "save_btn" to "Save",
        "before_after" to "Before / After",
        "undo" to "Undo",
        "redo" to "Redo",
        "reset" to "Reset",
        "original" to "Original",
        "edited" to "Edited",
        "zoom_in" to "Zoom +",
        "zoom_out" to "Zoom -",
        "rotate" to "Rotate",
        "compare_hint" to "স্লাইডার টেনে আগের ও পরের পার্থক্য দেখুন",

        // Editor Tool Tabs
        "tab_enhance" to "Auto Enhance",
        "tab_face" to "Face",
        "tab_bg" to "Background",
        "tab_lighting" to "Lighting",
        "tab_filters" to "Filters",
        "tab_effects" to "Effects",
        "tab_object" to "Remove Obj",
        "tab_restore" to "Enhancer",
        "tab_crop" to "Crop & Transform",
        "tab_text" to "Text & Sticker",
        "tab_draw" to "Draw",
        "tab_portrait" to "Portrait",
        "tab_pro" to "Professional",
        "tab_presets" to "Presets",

        // Face Tool
        "skin_smooth" to "Skin Smooth",
        "skin_tone" to "Skin Tone",
        "face_brightness" to "Face Brightness",
        "face_contrast" to "Face Contrast",
        "eye_brightness" to "Eye Brightness",
        "eye_detail" to "Eye Detail",
        "teeth_whitening" to "Teeth Whitening",
        "blemish_removal" to "Blemish Removal",
        "acne_reduction" to "Acne Reduction",
        "dark_circle" to "Dark Circle Reduction",
        "face_light" to "Face Light",
        "face_shadow" to "Face Shadow",
        "natural_beauty" to "Natural Beauty",
        "face_detail" to "Face Detail",

        // Background Tool
        "bg_blur" to "Background Blur",
        "blur_none" to "None",
        "blur_light" to "Light",
        "blur_med" to "Medium",
        "blur_strong" to "Strong",
        "bg_color" to "Background Color",
        "bg_replace" to "Background Replace",
        "custom_bg" to "Custom Background",
        "bg_remove" to "Remove Background",
        "transparent_bg" to "Transparent",
        "white_bg" to "White",

        // Lighting Tool
        "brightness" to "Brightness",
        "exposure" to "Exposure",
        "contrast" to "Contrast",
        "highlights" to "Highlights",
        "shadows" to "Shadows",
        "temperature" to "Temperature",
        "tint" to "Tint",
        "vibrance" to "Vibrance",
        "saturation" to "Saturation",
        "sharpness" to "Sharpness",
        "clarity" to "Clarity",

        // Object Removal
        "obj_hint" to "অপ্রয়োজনীয় অংশ বা মানুষের উপর ব্রাশ করুন",
        "brush_size" to "Brush Size",
        "remove_now" to "Remove Object",
        "clear_mask" to "Clear Mask",

        // Restore & Enhancer
        "ai_upscale" to "AI Upscale",
        "face_restore" to "Face Restoration",
        "scratch_remove" to "Scratch Removal",
        "noise_reduce" to "Noise Reduction",
        "bw_to_color" to "B&W to Color",

        // Export Screen
        "export_title" to "Export & Share",
        "save_to_device" to "ছবি সংরক্ষণ করুন",
        "share_photo" to "Share Photo",
        "quality_label" to "Quality",
        "format_label" to "Format",
        "resolution_label" to "Resolution",
        "size_label" to "File Size",
        "save_success" to "ছবি সফলভাবে সংরক্ষণ করা হয়েছে!",

        // Settings Screen
        "settings_title" to "Settings",
        "account" to "Account",
        "creator_profile" to "MI Creator",
        "pro_status" to "Pro AI Active",
        "app_settings" to "App Settings",
        "language_setting" to "Language (ভাষা)",
        "theme_setting" to "Dark Mode",
        "auto_save" to "Auto Save History",
        "ai_settings" to "AI Settings",
        "ai_quality" to "AI Quality Engine",
        "export_settings" to "Default Export Format",
        "privacy_policy" to "Privacy & Data Safety",
        "privacy_desc" to "আপনার ছবি পুরোপুরি নিরাপদ। প্রক্রিয়াকরণ সম্পূর্ণ হলে সার্ভার বা মেমোরি থেকে সাময়িক ছবি মুছে ফেলা হয়।",
        "about_app" to "About MI PHOTO EDITOR",
        "version" to "Version 1.0.0 (Build 2026)"
    )

    private val enMap = mapOf(
        "app_title" to "MI PHOTO EDITOR",
        "tagline" to "Make your photos stunning and professional with AI",
        "tagline_short" to "Edit Smarter. Look Better.",
        "upload_hero" to "Upload Photo",
        "upload_sub" to "Select from Gallery or take with Camera",
        "pick_gallery" to "Choose from Gallery",
        "take_camera" to "Take with Camera",
        "sample_photo" to "Try with Sample Photo",
        "ai_auto_mode" to "AI Auto Mode",
        "ai_auto_desc" to "AI automatically creates the best enhancement upon upload.",
        "ai_auto_btn" to "AI AUTO EDIT",
        "ai_auto_btn_sub" to "One-tap smart AI enhancement",
        "recent_edits" to "Recent Edits",
        "favorites" to "Favorites",
        "view_all" to "View All",
        "no_recents" to "No recent edits yet",
        "no_recents_desc" to "Upload a photo to start editing",
        "no_favs" to "No favorites saved",
        "quick_presets" to "Trending AI Presets",

        // Navigation
        "nav_home" to "Home",
        "nav_gallery" to "Gallery",
        "nav_edit" to "Edit",
        "nav_favorites" to "Favorites",
        "nav_profile" to "Profile",

        // AI Processing
        "analyzing_title" to "AI is analyzing your photo...",
        "step_face" to "Face Detecting",
        "step_bg" to "Background Detecting",
        "step_light" to "Lighting Optimizing",
        "step_skin" to "Skin Enhancing",
        "step_color" to "Color Correcting",
        "step_final" to "Finalizing",
        "ready_msg" to "Your photo is ready!",
        "natural_disclaimer" to "Natural & realistic preservation applied",

        // Editor Top & Common
        "save_btn" to "Save",
        "before_after" to "Before / After",
        "undo" to "Undo",
        "redo" to "Redo",
        "reset" to "Reset",
        "original" to "Original",
        "edited" to "Edited",
        "zoom_in" to "Zoom +",
        "zoom_out" to "Zoom -",
        "rotate" to "Rotate",
        "compare_hint" to "Drag slider to compare Before & After",

        // Editor Tool Tabs
        "tab_enhance" to "Auto Enhance",
        "tab_face" to "Face",
        "tab_bg" to "Background",
        "tab_lighting" to "Lighting",
        "tab_filters" to "Filters",
        "tab_effects" to "Effects",
        "tab_object" to "Remove Obj",
        "tab_restore" to "Enhancer",
        "tab_crop" to "Crop & Transform",
        "tab_text" to "Text & Sticker",
        "tab_draw" to "Draw",
        "tab_portrait" to "Portrait",
        "tab_pro" to "Professional",
        "tab_presets" to "Presets",

        // Face Tool
        "skin_smooth" to "Skin Smooth",
        "skin_tone" to "Skin Tone",
        "face_brightness" to "Face Brightness",
        "face_contrast" to "Face Contrast",
        "eye_brightness" to "Eye Brightness",
        "eye_detail" to "Eye Detail",
        "teeth_whitening" to "Teeth Whitening",
        "blemish_removal" to "Blemish Removal",
        "acne_reduction" to "Acne Reduction",
        "dark_circle" to "Dark Circle Reduction",
        "face_light" to "Face Light",
        "face_shadow" to "Face Shadow",
        "natural_beauty" to "Natural Beauty",
        "face_detail" to "Face Detail",

        // Background Tool
        "bg_blur" to "Background Blur",
        "blur_none" to "None",
        "blur_light" to "Light",
        "blur_med" to "Medium",
        "blur_strong" to "Strong",
        "bg_color" to "Background Color",
        "bg_replace" to "Background Replace",
        "custom_bg" to "Custom Background",
        "bg_remove" to "Remove Background",
        "transparent_bg" to "Transparent",
        "white_bg" to "White",

        // Lighting Tool
        "brightness" to "Brightness",
        "exposure" to "Exposure",
        "contrast" to "Contrast",
        "highlights" to "Highlights",
        "shadows" to "Shadows",
        "temperature" to "Temperature",
        "tint" to "Tint",
        "vibrance" to "Vibrance",
        "saturation" to "Saturation",
        "sharpness" to "Sharpness",
        "clarity" to "Clarity",

        // Object Removal
        "obj_hint" to "Brush over unwanted object or person to remove",
        "brush_size" to "Brush Size",
        "remove_now" to "Remove Object",
        "clear_mask" to "Clear Mask",

        // Restore & Enhancer
        "ai_upscale" to "AI Upscale",
        "face_restore" to "Face Restoration",
        "scratch_remove" to "Scratch Removal",
        "noise_reduce" to "Noise Reduction",
        "bw_to_color" to "B&W to Color",

        // Export Screen
        "export_title" to "Export & Share",
        "save_to_device" to "Save Photo",
        "share_photo" to "Share Photo",
        "quality_label" to "Quality",
        "format_label" to "Format",
        "resolution_label" to "Resolution",
        "size_label" to "File Size",
        "save_success" to "Photo saved successfully!",

        // Settings Screen
        "settings_title" to "Settings",
        "account" to "Account",
        "creator_profile" to "MI Creator",
        "pro_status" to "Pro AI Active",
        "app_settings" to "App Settings",
        "language_setting" to "Language",
        "theme_setting" to "Dark Mode",
        "auto_save" to "Auto Save History",
        "ai_settings" to "AI Settings",
        "ai_quality" to "AI Quality Engine",
        "export_settings" to "Default Export Format",
        "privacy_policy" to "Privacy & Data Safety",
        "privacy_desc" to "Your photos are strictly private and secure. All processing is transient and local/encrypted.",
        "about_app" to "About MI PHOTO EDITOR",
        "version" to "Version 1.0.0 (Build 2026)"
    )

    fun get(key: String, language: AppLanguage): String {
        return if (language == AppLanguage.BN) {
            bnMap[key] ?: enMap[key] ?: key
        } else {
            enMap[key] ?: bnMap[key] ?: key
        }
    }
}
