package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.Strings
import com.example.ui.screens.EditorScreen
import com.example.ui.screens.ExportScreen
import com.example.ui.screens.GalleryRecentsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProcessingScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.viewmodel.EditorViewModel
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.NavTab
import com.example.viewmodel.ScreenState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val editorViewModel: EditorViewModel = viewModel()

            val isDarkMode by mainViewModel.isDarkMode.collectAsState()
            val language by mainViewModel.language.collectAsState()
            val currentScreen by mainViewModel.currentScreen.collectAsState()
            val currentNavTab by mainViewModel.currentNavTab.collectAsState()
            val isAiAutoMode by mainViewModel.isAiAutoModeEnabled.collectAsState()
            val recentEdits by mainViewModel.recentEdits.collectAsState()
            val favoriteEdits by mainViewModel.favoriteEdits.collectAsState()

            val aiSteps by editorViewModel.aiSteps.collectAsState()
            val isAiReady by editorViewModel.isAiReady.collectAsState()
            val pendingBitmap by mainViewModel.pendingBitmap.collectAsState()

            MyApplicationTheme(darkTheme = isDarkMode) {
                // BackHandler behavior
                BackHandler(enabled = currentScreen != ScreenState.HOME || currentNavTab != NavTab.HOME) {
                    if (currentScreen != ScreenState.HOME) {
                        mainViewModel.navigateTo(ScreenState.HOME)
                    } else if (currentNavTab != NavTab.HOME) {
                        mainViewModel.selectNavTab(NavTab.HOME)
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("main_app_scaffold"),
                    contentWindowInsets = WindowInsets.safeDrawing,
                    bottomBar = {
                        // Only show bottom navigation on main tab screens
                        if (currentScreen == ScreenState.HOME) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.testTag("bottom_nav_bar")
                            ) {
                                NavigationBarItem(
                                    selected = currentNavTab == NavTab.HOME,
                                    onClick = { mainViewModel.selectNavTab(NavTab.HOME) },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                    label = { Text(Strings.get("nav_home", language), fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = ElectricViolet,
                                        selectedTextColor = ElectricViolet,
                                        indicatorColor = ElectricViolet.copy(alpha = 0.15f)
                                    ),
                                    modifier = Modifier.testTag("nav_item_home")
                                )

                                NavigationBarItem(
                                    selected = currentNavTab == NavTab.GALLERY,
                                    onClick = { mainViewModel.selectNavTab(NavTab.GALLERY) },
                                    icon = { Icon(Icons.Default.PhotoLibrary, contentDescription = "Gallery") },
                                    label = { Text(Strings.get("nav_gallery", language), fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = ElectricViolet,
                                        selectedTextColor = ElectricViolet,
                                        indicatorColor = ElectricViolet.copy(alpha = 0.15f)
                                    ),
                                    modifier = Modifier.testTag("nav_item_gallery")
                                )

                                NavigationBarItem(
                                    selected = currentNavTab == NavTab.EDIT,
                                    onClick = {
                                        mainViewModel.loadSamplePortrait()
                                    },
                                    icon = { Icon(Icons.Default.Edit, contentDescription = "Edit") },
                                    label = { Text(Strings.get("nav_edit", language), fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = ElectricViolet,
                                        selectedTextColor = ElectricViolet,
                                        indicatorColor = ElectricViolet.copy(alpha = 0.15f)
                                    ),
                                    modifier = Modifier.testTag("nav_item_edit")
                                )

                                NavigationBarItem(
                                    selected = currentNavTab == NavTab.FAVORITES,
                                    onClick = { mainViewModel.selectNavTab(NavTab.FAVORITES) },
                                    icon = { Icon(Icons.Default.Star, contentDescription = "Favorites") },
                                    label = { Text(Strings.get("nav_favorites", language), fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = ElectricViolet,
                                        selectedTextColor = ElectricViolet,
                                        indicatorColor = ElectricViolet.copy(alpha = 0.15f)
                                    ),
                                    modifier = Modifier.testTag("nav_item_favorites")
                                )

                                NavigationBarItem(
                                    selected = currentNavTab == NavTab.PROFILE,
                                    onClick = { mainViewModel.selectNavTab(NavTab.PROFILE) },
                                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                                    label = { Text(Strings.get("nav_profile", language), fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = ElectricViolet,
                                        selectedTextColor = ElectricViolet,
                                        indicatorColor = ElectricViolet.copy(alpha = 0.15f)
                                    ),
                                    modifier = Modifier.testTag("nav_item_profile")
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "ScreenTransition"
                        ) { screen ->
                            when (screen) {
                                ScreenState.HOME -> {
                                    when (currentNavTab) {
                                        NavTab.HOME -> {
                                            HomeScreen(
                                                language = language,
                                                isAiAutoMode = isAiAutoMode,
                                                recentEdits = recentEdits,
                                                favorites = favoriteEdits,
                                                onToggleAiAutoMode = { mainViewModel.toggleAiAutoMode() },
                                                onPhotoSelected = { uri ->
                                                    mainViewModel.loadUriForEditing(uri)
                                                },
                                                onCameraCapture = { bmp ->
                                                    mainViewModel.startEditingWithBitmap(bmp)
                                                },
                                                onSamplePhotoClick = {
                                                    mainViewModel.loadSamplePortrait()
                                                },
                                                onContinueRecentEdit = { edit ->
                                                    mainViewModel.continueRecentEdit(edit)
                                                },
                                                onToggleFavorite = { edit ->
                                                    mainViewModel.toggleFavorite(edit)
                                                },
                                                onDeleteRecentEdit = { edit ->
                                                    mainViewModel.deleteRecentEdit(edit)
                                                },
                                                onShareEdit = { edit ->
                                                    mainViewModel.shareExportedPhoto(java.io.File(edit.imagePath))
                                                }
                                            )
                                        }
                                        NavTab.GALLERY, NavTab.FAVORITES -> {
                                            GalleryRecentsScreen(
                                                language = language,
                                                recentEdits = recentEdits,
                                                favorites = favoriteEdits,
                                                onOpenEdit = { edit -> mainViewModel.continueRecentEdit(edit) },
                                                onToggleFavorite = { edit -> mainViewModel.toggleFavorite(edit) },
                                                onDeleteEdit = { edit -> mainViewModel.deleteRecentEdit(edit) },
                                                onShareEdit = { edit -> mainViewModel.shareExportedPhoto(java.io.File(edit.imagePath)) }
                                            )
                                        }
                                        NavTab.PROFILE -> {
                                            SettingsScreen(
                                                language = language,
                                                isDarkMode = isDarkMode,
                                                isAiAutoMode = isAiAutoMode,
                                                onToggleLanguage = { mainViewModel.toggleLanguage() },
                                                onToggleDarkMode = { mainViewModel.toggleDarkMode() },
                                                onToggleAiAutoMode = { mainViewModel.toggleAiAutoMode() }
                                            )
                                        }
                                        NavTab.EDIT -> {
                                            // Fallback to home if edit selected with no image
                                            HomeScreen(
                                                language = language,
                                                isAiAutoMode = isAiAutoMode,
                                                recentEdits = recentEdits,
                                                favorites = favoriteEdits,
                                                onToggleAiAutoMode = { mainViewModel.toggleAiAutoMode() },
                                                onPhotoSelected = { uri -> mainViewModel.loadUriForEditing(uri) },
                                                onCameraCapture = { bmp -> mainViewModel.startEditingWithBitmap(bmp) },
                                                onSamplePhotoClick = { mainViewModel.loadSamplePortrait() },
                                                onContinueRecentEdit = { edit -> mainViewModel.continueRecentEdit(edit) },
                                                onToggleFavorite = { edit -> mainViewModel.toggleFavorite(edit) },
                                                onDeleteRecentEdit = { edit -> mainViewModel.deleteRecentEdit(edit) },
                                                onShareEdit = { edit -> mainViewModel.shareExportedPhoto(java.io.File(edit.imagePath)) }
                                            )
                                        }
                                    }
                                }

                                ScreenState.PROCESSING -> {
                                    // Start processing when in PROCESSING screen
                                    androidx.compose.runtime.LaunchedEffect(pendingBitmap) {
                                        pendingBitmap?.let { bmp ->
                                            editorViewModel.loadSourceBitmap(bmp, autoEnhance = true)
                                        }
                                    }

                                    ProcessingScreen(
                                        previewBitmap = pendingBitmap,
                                        steps = aiSteps,
                                        isReady = isAiReady,
                                        language = language,
                                        onContinueToEditor = {
                                            mainViewModel.navigateTo(ScreenState.EDITOR)
                                        }
                                    )
                                }

                                ScreenState.EDITOR -> {
                                    androidx.compose.runtime.LaunchedEffect(pendingBitmap) {
                                        if (editorViewModel.originalBitmap.value == null) {
                                            pendingBitmap?.let { bmp ->
                                                editorViewModel.loadSourceBitmap(bmp, autoEnhance = isAiAutoMode)
                                            }
                                        }
                                    }

                                    EditorScreen(
                                        editorViewModel = editorViewModel,
                                        language = language,
                                        onBack = {
                                            mainViewModel.navigateTo(ScreenState.HOME)
                                        },
                                        onSaveClick = {
                                            mainViewModel.navigateTo(ScreenState.EXPORT)
                                        }
                                    )
                                }

                                ScreenState.EXPORT -> {
                                    ExportScreen(
                                        editorViewModel = editorViewModel,
                                        language = language,
                                        onBack = {
                                            mainViewModel.navigateTo(ScreenState.EDITOR)
                                        },
                                        onPhotoSaved = { file ->
                                            mainViewModel.setExportedFile(file)
                                        },
                                        onShareClick = { file ->
                                            mainViewModel.shareExportedPhoto(file)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
