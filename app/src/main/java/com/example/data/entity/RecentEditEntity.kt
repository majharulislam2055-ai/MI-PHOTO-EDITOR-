package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_edits")
data class RecentEditEntity(
    @PrimaryKey
    val id: String,
    val imagePath: String,
    val originalImagePath: String? = null,
    val title: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val resolution: String = "1080 x 1440",
    val filterApplied: String = "Natural",
    val presetApplied: String? = null
)
