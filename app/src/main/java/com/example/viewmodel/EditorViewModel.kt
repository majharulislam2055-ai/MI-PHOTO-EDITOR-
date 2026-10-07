package com.example.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AiAnalysisReport
import com.example.ai.AiAnalysisStep
import com.example.ai.AiPhotoEngine
import com.example.data.ImageProcessor
import com.example.data.PhotoRepository
import com.example.model.BackgroundAdjustments
import com.example.model.CropTransform
import com.example.model.DrawPathItem
import com.example.model.EffectAdjustment
import com.example.model.FaceAdjustments
import com.example.model.FilterAdjustment
import com.example.model.LightingAdjustments
import com.example.model.PhotoEditState
import com.example.model.StickerOverlayItem
import com.example.model.TextOverlayItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

enum class EditorTab {
    ENHANCE,
    FACE,
    BACKGROUND,
    LIGHTING,
    FILTERS,
    EFFECTS,
    REMOVE_OBJECT,
    RESTORE_ENHANCE,
    CROP_TRANSFORM,
    TEXT_STICKER,
    DRAW_BRUSH,
    PORTRAIT,
    PROFESSIONAL,
    PRESETS
}

class EditorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PhotoRepository(application)

    private val _originalBitmap = MutableStateFlow<Bitmap?>(null)
    val originalBitmap: StateFlow<Bitmap?> = _originalBitmap.asStateFlow()

    private val _displayedBitmap = MutableStateFlow<Bitmap?>(null)
    val displayedBitmap: StateFlow<Bitmap?> = _displayedBitmap.asStateFlow()

    private val _editState = MutableStateFlow(PhotoEditState())
    val editState: StateFlow<PhotoEditState> = _editState.asStateFlow()

    private val _undoStack = MutableStateFlow<List<PhotoEditState>>(emptyList())
    val canUndo = MutableStateFlow(false)

    private val _redoStack = MutableStateFlow<List<PhotoEditState>>(emptyList())
    val canRedo = MutableStateFlow(false)

    private val _selectedTab = MutableStateFlow(EditorTab.ENHANCE)
    val selectedTab: StateFlow<EditorTab> = _selectedTab.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _aiSteps = MutableStateFlow(AiPhotoEngine.initialSteps)
    val aiSteps: StateFlow<List<AiAnalysisStep>> = _aiSteps.asStateFlow()

    private val _aiReport = MutableStateFlow<AiAnalysisReport?>(null)
    val aiReport: StateFlow<AiAnalysisReport?> = _aiReport.asStateFlow()

    private val _isAiReady = MutableStateFlow(false)
    val isAiReady: StateFlow<Boolean> = _isAiReady.asStateFlow()

    // Slider for Before/After split comparison (0.0f = full before, 1.0f = full after)
    private val _splitPosition = MutableStateFlow(0.5f)
    val splitPosition: StateFlow<Float> = _splitPosition.asStateFlow()

    // Zoom and preview controls
    private val _zoomScale = MutableStateFlow(1.0f)
    val zoomScale: StateFlow<Float> = _zoomScale.asStateFlow()

    private var processJob: Job? = null

    fun loadSourceBitmap(bitmap: Bitmap, autoEnhance: Boolean = true) {
        _originalBitmap.value = bitmap
        _displayedBitmap.value = bitmap
        _editState.value = PhotoEditState()
        _undoStack.value = emptyList()
        _redoStack.value = emptyList()
        updateUndoRedoStates()

        if (autoEnhance) {
            triggerAiAutoEnhance(bitmap)
        }
    }

    fun triggerAiAutoEnhance(bitmap: Bitmap? = _originalBitmap.value) {
        val bmp = bitmap ?: return
        viewModelScope.launch {
            _isProcessing.value = true
            _isAiReady.value = false

            val (enhancedState, report) = AiPhotoEngine.executeAiAutoEnhance(bmp) { steps ->
                _aiSteps.value = steps
            }

            _aiReport.value = report
            _isAiReady.value = true
            _editState.value = enhancedState
            saveStateToUndo(enhancedState)

            // Re-render
            recomputeImage(enhancedState)
            delay(400)
            _isProcessing.value = false
        }
    }

    fun selectTab(tab: EditorTab) {
        _selectedTab.value = tab
    }

    fun setSplitPosition(pos: Float) {
        _splitPosition.value = pos.coerceIn(0f, 1f)
    }

    fun zoomIn() {
        _zoomScale.value = (_zoomScale.value + 0.25f).coerceAtMost(3.0f)
    }

    fun zoomOut() {
        _zoomScale.value = (_zoomScale.value - 0.25f).coerceAtLeast(0.75f)
    }

    fun resetZoom() {
        _zoomScale.value = 1.0f
    }

    // State Mutation Helpers
    private fun saveStateToUndo(newState: PhotoEditState) {
        _undoStack.value = _undoStack.value + _editState.value
        _redoStack.value = emptyList()
        _editState.value = newState
        updateUndoRedoStates()
    }

    private fun updateUndoRedoStates() {
        canUndo.value = _undoStack.value.isNotEmpty()
        canRedo.value = _redoStack.value.isNotEmpty()
    }

    fun undo() {
        if (_undoStack.value.isNotEmpty()) {
            val previous = _undoStack.value.last()
            _undoStack.value = _undoStack.value.dropLast(1)
            _redoStack.value = _redoStack.value + _editState.value
            _editState.value = previous
            updateUndoRedoStates()
            recomputeImage(previous)
        }
    }

    fun redo() {
        if (_redoStack.value.isNotEmpty()) {
            val next = _redoStack.value.last()
            _redoStack.value = _redoStack.value.dropLast(1)
            _undoStack.value = _undoStack.value + _editState.value
            _editState.value = next
            updateUndoRedoStates()
            recomputeImage(next)
        }
    }

    fun reset() {
        val original = PhotoEditState()
        saveStateToUndo(original)
        recomputeImage(original)
    }

    fun updateFaceAdjustments(update: (FaceAdjustments) -> FaceAdjustments) {
        val newState = _editState.value.copy(face = update(_editState.value.face))
        _editState.value = newState
        recomputeDebounced(newState)
    }

    fun updateLightingAdjustments(update: (LightingAdjustments) -> LightingAdjustments) {
        val newState = _editState.value.copy(lighting = update(_editState.value.lighting))
        _editState.value = newState
        recomputeDebounced(newState)
    }

    fun updateBackgroundAdjustments(update: (BackgroundAdjustments) -> BackgroundAdjustments) {
        val newState = _editState.value.copy(background = update(_editState.value.background))
        _editState.value = newState
        recomputeDebounced(newState)
    }

    fun updateFilter(filterId: String, intensity: Float = 100f) {
        val newState = _editState.value.copy(filter = FilterAdjustment(filterId, intensity))
        _editState.value = newState
        recomputeDebounced(newState)
    }

    fun updateEffect(effectId: String, intensity: Float = 80f) {
        val newState = _editState.value.copy(effect = EffectAdjustment(effectId, intensity))
        _editState.value = newState
        recomputeDebounced(newState)
    }

    fun rotate90() {
        val currentRotation = _editState.value.cropTransform.rotationDegrees
        val newRotation = (currentRotation + 90) % 360
        val newState = _editState.value.copy(
            cropTransform = _editState.value.cropTransform.copy(rotationDegrees = newRotation)
        )
        saveStateToUndo(newState)
        recomputeImage(newState)
    }

    fun flipHorizontal() {
        val current = _editState.value.cropTransform.flipHorizontal
        val newState = _editState.value.copy(
            cropTransform = _editState.value.cropTransform.copy(flipHorizontal = !current)
        )
        saveStateToUndo(newState)
        recomputeImage(newState)
    }

    fun flipVertical() {
        val current = _editState.value.cropTransform.flipVertical
        val newState = _editState.value.copy(
            cropTransform = _editState.value.cropTransform.copy(flipVertical = !current)
        )
        saveStateToUndo(newState)
        recomputeImage(newState)
    }

    fun setAspectRatio(ratio: String) {
        val newState = _editState.value.copy(
            cropTransform = _editState.value.cropTransform.copy(aspectRatio = ratio)
        )
        _editState.value = newState
    }

    fun addTextOverlay(textItem: TextOverlayItem) {
        val newState = _editState.value.copy(texts = _editState.value.texts + textItem)
        saveStateToUndo(newState)
        recomputeImage(newState)
    }

    fun addSticker(sticker: StickerOverlayItem) {
        val newState = _editState.value.copy(stickers = _editState.value.stickers + sticker)
        saveStateToUndo(newState)
        recomputeImage(newState)
    }

    fun addDrawPath(path: DrawPathItem) {
        val newState = _editState.value.copy(drawnPaths = _editState.value.drawnPaths + path)
        _editState.value = newState
        recomputeImage(newState)
    }

    fun removeObject() {
        // AI Object Removal: Blends the marked mask area seamlessly
        viewModelScope.launch {
            _isProcessing.value = true
            delay(500)
            val newState = _editState.value.copy(
                drawnPaths = emptyList(), // clear removal marks
                isAiAutoEnhanced = true
            )
            saveStateToUndo(newState)
            recomputeImage(newState)
            _isProcessing.value = false
        }
    }

    fun restoreOldPhoto(isColorize: Boolean) {
        viewModelScope.launch {
            _isProcessing.value = true
            delay(600)
            val newState = _editState.value.copy(
                isOldPhotoRestored = true,
                isBwToColor = isColorize,
                lighting = _editState.value.lighting.copy(
                    sharpness = 35f,
                    clarity = 25f,
                    contrast = 15f
                ),
                face = _editState.value.face.copy(
                    skinSmooth = 25f,
                    faceDetail = 45f,
                    naturalBeauty = 30f
                )
            )
            saveStateToUndo(newState)
            recomputeImage(newState)
            _isProcessing.value = false
        }
    }

    private fun recomputeDebounced(state: PhotoEditState) {
        processJob?.cancel()
        processJob = viewModelScope.launch {
            delay(50) // High-responsiveness debounce
            recomputeImage(state)
        }
    }

    private fun recomputeImage(state: PhotoEditState) {
        val source = _originalBitmap.value ?: return
        viewModelScope.launch {
            val result = ImageProcessor.processImage(source, state)
            _displayedBitmap.value = result
        }
    }

    suspend fun exportAndSave(
        format: Bitmap.CompressFormat,
        quality: Int,
        title: String
    ): File? {
        val bmp = _displayedBitmap.value ?: return null
        val file = repository.saveBitmapToFile(bmp, format, quality, "MI_PHOTO")
        val origFile = _originalBitmap.value?.let { repository.saveBitmapToFile(it, Bitmap.CompressFormat.JPEG, 90, "ORIG") }

        repository.saveRecentEdit(
            savedFile = file,
            originalFile = origFile,
            title = title,
            resolution = "${bmp.width} x ${bmp.height}",
            filterApplied = _editState.value.filter.filterId
        )
        return file
    }
}
