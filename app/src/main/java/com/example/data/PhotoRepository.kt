package com.example.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.data.database.AppDatabase
import com.example.data.entity.RecentEditEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class PhotoRepository(private val context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val dao = db.recentEditDao()

    val allRecentEdits: Flow<List<RecentEditEntity>> = dao.getAllRecentEdits()
    val favoriteEdits: Flow<List<RecentEditEntity>> = dao.getFavoriteEdits()

    suspend fun loadBitmapFromUri(uri: Uri): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun loadBitmapFromDrawable(resId: Int): Bitmap? = withContext(Dispatchers.IO) {
        try {
            BitmapFactory.decodeResource(context.resources, resId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun loadBitmapFromFile(filePath: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val file = File(filePath)
            if (file.exists()) {
                BitmapFactory.decodeFile(file.absolutePath)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun saveBitmapToFile(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 95,
        filenamePrefix: String = "MI_EDIT"
    ): File = withContext(Dispatchers.IO) {
        val dir = File(context.filesDir, "images")
        if (!dir.exists()) dir.mkdirs()

        val extension = when (format) {
            Bitmap.CompressFormat.PNG -> "png"
            Bitmap.CompressFormat.WEBP, Bitmap.CompressFormat.WEBP_LOSSLESS -> "webp"
            else -> "jpg"
        }
        val file = File(dir, "${filenamePrefix}_${System.currentTimeMillis()}.$extension")
        FileOutputStream(file).use { out ->
            bitmap.compress(format, quality.coerceIn(10, 100), out)
        }
        file
    }

    suspend fun saveRecentEdit(
        savedFile: File,
        originalFile: File? = null,
        title: String = "MI Photo",
        resolution: String = "1080 x 1440",
        filterApplied: String = "Natural",
        isFavorite: Boolean = false
    ): RecentEditEntity = withContext(Dispatchers.IO) {
        val entity = RecentEditEntity(
            id = UUID.randomUUID().toString(),
            imagePath = savedFile.absolutePath,
            originalImagePath = originalFile?.absolutePath,
            title = title,
            timestamp = System.currentTimeMillis(),
            isFavorite = isFavorite,
            resolution = resolution,
            filterApplied = filterApplied
        )
        dao.insertEdit(entity)
        entity
    }

    suspend fun toggleFavorite(id: String, isFav: Boolean) = withContext(Dispatchers.IO) {
        dao.updateFavoriteStatus(id, isFav)
    }

    suspend fun deleteEdit(entity: RecentEditEntity) = withContext(Dispatchers.IO) {
        try {
            val file = File(entity.imagePath)
            if (file.exists()) file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        dao.deleteEdit(entity)
    }

    fun getShareableUri(file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}
