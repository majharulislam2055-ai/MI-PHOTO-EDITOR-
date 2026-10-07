package com.example.viewmodel

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.PhotoRepository
import com.example.data.entity.RecentEditEntity
import com.example.model.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

enum class ScreenState {
    HOME,
    PROCESSING,
    EDITOR,
    EXPORT
}

enum class NavTab {
    HOME,
    GALLERY,
    EDIT,
    FAVORITES,
    PROFILE
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PhotoRepository(application)

    private val _currentScreen = MutableStateFlow(ScreenState.HOME)
    val currentScreen: StateFlow<ScreenState> = _currentScreen.asStateFlow()

    private val _currentNavTab = MutableStateFlow(NavTab.HOME)
    val currentNavTab: StateFlow<NavTab> = _currentNavTab.asStateFlow()

    private val _language = MutableStateFlow(AppLanguage.BN) // Default to Bengali as requested
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _isDarkMode = MutableStateFlow(true) // Premium dark theme by default
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isAiAutoModeEnabled = MutableStateFlow(true) // Default ON as requested
    val isAiAutoModeEnabled: StateFlow<Boolean> = _isAiAutoModeEnabled.asStateFlow()

    private val _pendingBitmap = MutableStateFlow<Bitmap?>(null)
    val pendingBitmap: StateFlow<Bitmap?> = _pendingBitmap.asStateFlow()

    private val _lastExportedFile = MutableStateFlow<File?>(null)
    val lastExportedFile: StateFlow<File?> = _lastExportedFile.asStateFlow()

    val recentEdits: StateFlow<List<RecentEditEntity>> = repository.allRecentEdits.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteEdits: StateFlow<List<RecentEditEntity>> = repository.favoriteEdits.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun navigateTo(screen: ScreenState) {
        _currentScreen.value = screen
    }

    fun selectNavTab(tab: NavTab) {
        _currentNavTab.value = tab
        if (_currentScreen.value != ScreenState.HOME && tab == NavTab.HOME) {
            _currentScreen.value = ScreenState.HOME
        }
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.BN) AppLanguage.EN else AppLanguage.BN
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun toggleAiAutoMode() {
        _isAiAutoModeEnabled.value = !_isAiAutoModeEnabled.value
    }

    fun startEditingWithBitmap(bitmap: Bitmap, autoEnhance: Boolean = _isAiAutoModeEnabled.value) {
        _pendingBitmap.value = bitmap
        if (autoEnhance) {
            _currentScreen.value = ScreenState.PROCESSING
        } else {
            _currentScreen.value = ScreenState.EDITOR
        }
    }

    fun loadUriForEditing(uri: Uri) {
        viewModelScope.launch {
            val bitmap = repository.loadBitmapFromUri(uri)
            if (bitmap != null) {
                startEditingWithBitmap(bitmap)
            }
        }
    }

    fun loadSamplePortrait() {
        viewModelScope.launch {
            val bitmap = repository.loadBitmapFromDrawable(R.drawable.sample_portrait_1)
            if (bitmap != null) {
                startEditingWithBitmap(bitmap)
            }
        }
    }

    fun continueRecentEdit(edit: RecentEditEntity) {
        viewModelScope.launch {
            val bitmap = repository.loadBitmapFromFile(edit.imagePath)
            if (bitmap != null) {
                _pendingBitmap.value = bitmap
                _currentScreen.value = ScreenState.EDITOR
            }
        }
    }

    fun toggleFavorite(edit: RecentEditEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(edit.id, !edit.isFavorite)
        }
    }

    fun deleteRecentEdit(edit: RecentEditEntity) {
        viewModelScope.launch {
            repository.deleteEdit(edit)
        }
    }

    fun setExportedFile(file: File) {
        _lastExportedFile.value = file
        _currentScreen.value = ScreenState.EXPORT
    }

    fun shareExportedPhoto(file: File? = _lastExportedFile.value) {
        val f = file ?: return
        try {
            val uri = repository.getShareableUri(f)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val chooser = Intent.createChooser(intent, "Share MI Photo with:").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            getApplication<Application>().startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
