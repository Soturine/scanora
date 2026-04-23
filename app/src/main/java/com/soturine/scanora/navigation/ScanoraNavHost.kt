package com.soturine.scanora.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.soturine.scanora.app.AppContainer
import com.soturine.scanora.app.RootViewModel
import com.soturine.scanora.core.common.model.ExportedFile
import com.soturine.scanora.core.common.model.ScanMode
import com.soturine.scanora.feature.camera.CameraCaptureScreen
import com.soturine.scanora.feature.camera.CameraCaptureViewModel
import com.soturine.scanora.feature.editor.CropScreen
import com.soturine.scanora.feature.editor.EditorViewModel
import com.soturine.scanora.feature.editor.FilterScreen
import com.soturine.scanora.feature.editor.ReviewScreen
import com.soturine.scanora.feature.export.ExportScreen
import com.soturine.scanora.feature.export.ExportViewModel
import com.soturine.scanora.feature.history.HistoryScreen
import com.soturine.scanora.feature.history.HistoryViewModel
import com.soturine.scanora.feature.history.ScanDetailScreen
import com.soturine.scanora.feature.history.ScanDetailViewModel
import com.soturine.scanora.feature.home.HomeScreen
import com.soturine.scanora.feature.home.HomeViewModel
import com.soturine.scanora.feature.ocr.OcrScreen
import com.soturine.scanora.feature.ocr.OcrViewModel
import com.soturine.scanora.feature.settings.AboutScreen
import com.soturine.scanora.feature.settings.SettingsScreen
import com.soturine.scanora.feature.settings.SettingsViewModel
import com.soturine.scanora.onboarding.OnboardingScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun ScanoraNavHost(
    container: AppContainer,
    rootViewModel: RootViewModel,
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val rootState = rootViewModel.uiState.collectAsStateWithLifecycle()
    val startDestination = if (rootState.value.onboardingCompleted) {
        ScanoraDestinations.Home
    } else {
        ScanoraDestinations.Onboarding
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(ScanoraDestinations.Onboarding) {
            OnboardingScreen(
                onFinish = {
                    rootViewModel.completeOnboarding()
                    navController.navigate(ScanoraDestinations.Home) {
                        popUpTo(ScanoraDestinations.Onboarding) { inclusive = true }
                    }
                },
            )
        }
        composable(ScanoraDestinations.Home) {
            val homeViewModel: HomeViewModel = featureViewModel {
                HomeViewModel(
                    scanRepository = container.scanRepository,
                    preferencesRepository = container.userPreferencesRepository,
                )
            }
            val state = homeViewModel.uiState.collectAsStateWithLifecycle()
            HomeScreen(
                state = state.value,
                onStartQuickScan = { mode, uris ->
                    coroutineScope.launch {
                        createDraftScan(
                            container = container,
                            mode = mode,
                            uris = uris,
                        )?.let { (scanId, _) ->
                            navController.navigate(ScanoraDestinations.review(scanId))
                        }
                    }
                },
                onOpenManualCamera = { mode ->
                    navController.navigate(ScanoraDestinations.camera(mode))
                },
                onImportImages = { mode, uris ->
                    coroutineScope.launch {
                        createDraftScan(
                            container = container,
                            mode = mode,
                            uris = uris,
                        )?.let { (scanId, pageId) ->
                            navController.navigate(ScanoraDestinations.crop(scanId, pageId))
                        }
                    }
                },
                onModeSelected = homeViewModel::onModeSelected,
                onQueryChange = homeViewModel::onQueryChange,
                onOpenHistory = { navController.navigate(ScanoraDestinations.History) },
                onOpenSettings = { navController.navigate(ScanoraDestinations.Settings) },
                onOpenScan = { scanId ->
                    navController.navigate(ScanoraDestinations.detail(scanId))
                },
            )
        }
        composable(
            route = ScanoraDestinations.Camera,
            arguments = listOf(navArgument("mode") { type = NavType.StringType }),
        ) { entry ->
            val mode = ScanMode.fromStorageKey(entry.arguments?.getString("mode").orEmpty())
            val cameraViewModel: CameraCaptureViewModel = featureViewModel(key = "camera-${mode.storageKey}") {
                CameraCaptureViewModel(mode)
            }
            val state = cameraViewModel.uiState.collectAsStateWithLifecycle()
            CameraCaptureScreen(
                state = state.value,
                onPermissionResult = cameraViewModel::onPermissionResult,
                onCapturedImage = { uri ->
                    coroutineScope.launch {
                        createDraftScan(
                            container = container,
                            mode = mode,
                            uris = listOf(uri),
                        )?.let { (scanId, pageId) ->
                            navController.navigate(ScanoraDestinations.crop(scanId, pageId))
                        }
                    }
                },
                onCapturedBatch = { uris ->
                    coroutineScope.launch {
                        createDraftScan(
                            container = container,
                            mode = mode,
                            uris = uris,
                        )?.let { (scanId, pageId) ->
                            navController.navigate(ScanoraDestinations.crop(scanId, pageId))
                        }
                    }
                },
                onBack = { navController.popBackStack() },
                onCaptureStarted = cameraViewModel::onCaptureStarted,
                onCaptureFinished = cameraViewModel::onCaptureFinished,
                onError = cameraViewModel::onError,
            )
        }
        composable(
            route = ScanoraDestinations.Crop,
            arguments = listOf(
                navArgument("scanId") { type = NavType.StringType },
                navArgument("pageId") { type = NavType.StringType },
            ),
        ) { entry ->
            val scanId = entry.arguments?.getString("scanId").orEmpty()
            val pageId = entry.arguments?.getString("pageId").orEmpty()
            val editorViewModel: EditorViewModel = featureViewModel(key = "crop-$scanId-$pageId") {
                EditorViewModel(
                    scanId = scanId,
                    initialPageId = pageId,
                    scanRepository = container.scanRepository,
                    processingRepository = container.documentProcessingRepository,
                )
            }
            val state = editorViewModel.uiState.collectAsStateWithLifecycle()
            CropScreen(
                state = state.value,
                onSaveQuad = editorViewModel::updateQuad,
                onContinue = {
                    navController.navigate(ScanoraDestinations.filters(scanId, pageId))
                },
                onEnsureQuad = editorViewModel::ensureQuadForCurrentPage,
                onReestimate = editorViewModel::reestimateCurrentPageQuad,
                onBack = { navController.popBackStack() },
                onClearMessage = editorViewModel::clearMessage,
            )
        }
        composable(
            route = ScanoraDestinations.Filters,
            arguments = listOf(
                navArgument("scanId") { type = NavType.StringType },
                navArgument("pageId") { type = NavType.StringType },
            ),
        ) { entry ->
            val scanId = entry.arguments?.getString("scanId").orEmpty()
            val pageId = entry.arguments?.getString("pageId").orEmpty()
            val editorViewModel: EditorViewModel = featureViewModel(key = "filter-$scanId-$pageId") {
                EditorViewModel(
                    scanId = scanId,
                    initialPageId = pageId,
                    scanRepository = container.scanRepository,
                    processingRepository = container.documentProcessingRepository,
                )
            }
            val state = editorViewModel.uiState.collectAsStateWithLifecycle()
            FilterScreen(
                state = state.value,
                onApplyFilter = editorViewModel::applyFilter,
                onRequestPreview = editorViewModel::prepareFilterPreview,
                onRotate = editorViewModel::rotateCurrentPage,
                onOpenReview = { navController.navigate(ScanoraDestinations.review(scanId)) },
                onBack = { navController.popBackStack() },
                onClearMessage = editorViewModel::clearMessage,
            )
        }
        composable(
            route = ScanoraDestinations.Review,
            arguments = listOf(navArgument("scanId") { type = NavType.StringType }),
        ) { entry ->
            val scanId = entry.arguments?.getString("scanId").orEmpty()
            val editorViewModel: EditorViewModel = featureViewModel(key = "review-$scanId") {
                EditorViewModel(
                    scanId = scanId,
                    initialPageId = null,
                    scanRepository = container.scanRepository,
                    processingRepository = container.documentProcessingRepository,
                )
            }
            val state = editorViewModel.uiState.collectAsStateWithLifecycle()
            ReviewScreen(
                state = state.value,
                onRename = editorViewModel::renameScan,
                onUpdateTags = editorViewModel::updateTags,
                onClearMessage = editorViewModel::clearMessage,
                onSelectPage = editorViewModel::selectPage,
                onMovePageUp = { editorViewModel.movePage(it, -1) },
                onMovePageDown = { editorViewModel.movePage(it, 1) },
                onDeleteCurrentPage = editorViewModel::deleteCurrentPage,
                onOpenCrop = {
                    state.value.currentPage?.id?.let { pageId ->
                        navController.navigate(ScanoraDestinations.crop(scanId, pageId))
                    }
                },
                onOpenFilters = {
                    state.value.currentPage?.id?.let { pageId ->
                        navController.navigate(ScanoraDestinations.filters(scanId, pageId))
                    }
                },
                onOpenExport = { navController.navigate(ScanoraDestinations.export(scanId)) },
                onOpenOcr = { pageId ->
                    navController.navigate(ScanoraDestinations.ocr(scanId, pageId))
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable(ScanoraDestinations.History) {
            val historyViewModel: HistoryViewModel = featureViewModel {
                HistoryViewModel(container.scanRepository)
            }
            val state = historyViewModel.uiState.collectAsStateWithLifecycle()
            HistoryScreen(
                state = state.value,
                onQueryChange = historyViewModel::onQueryChange,
                onOpenScan = { scanId ->
                    navController.navigate(ScanoraDestinations.detail(scanId))
                },
            )
        }
        composable(
            route = ScanoraDestinations.Detail,
            arguments = listOf(navArgument("scanId") { type = NavType.StringType }),
        ) { entry ->
            val scanId = entry.arguments?.getString("scanId").orEmpty()
            val detailViewModel: ScanDetailViewModel = featureViewModel(key = "detail-$scanId") {
                ScanDetailViewModel(
                    scanId = scanId,
                    scanRepository = container.scanRepository,
                )
            }
            val state = detailViewModel.scan.collectAsStateWithLifecycle()
            ScanDetailScreen(
                scan = state.value,
                onToggleFavorite = detailViewModel::toggleFavorite,
                onDeleteScan = {
                    detailViewModel.deleteScan()
                    navController.popBackStack()
                },
                onOpenReview = { navController.navigate(ScanoraDestinations.review(scanId)) },
                onOpenExport = { navController.navigate(ScanoraDestinations.export(scanId)) },
                onOpenOcr = { pageId -> navController.navigate(ScanoraDestinations.ocr(scanId, pageId)) },
            )
        }
        composable(
            route = ScanoraDestinations.Export,
            arguments = listOf(navArgument("scanId") { type = NavType.StringType }),
        ) { entry ->
            val scanId = entry.arguments?.getString("scanId").orEmpty()
            val exportViewModel: ExportViewModel = featureViewModel(key = "export-$scanId") {
                ExportViewModel(
                    scanId = scanId,
                    scanRepository = container.scanRepository,
                    preferencesRepository = container.userPreferencesRepository,
                    exportRepository = container.exportRepository,
                )
            }
            val state = exportViewModel.uiState.collectAsStateWithLifecycle()
            ExportScreen(
                state = state.value,
                onSelectFormat = exportViewModel::selectFormat,
                onSelectQuality = exportViewModel::selectQuality,
                onExport = exportViewModel::export,
                onShare = { files -> shareFiles(context, files) },
                onOpenFile = { file -> openExportedFile(context, file) },
                onBack = { navController.popBackStack() },
                onClearMessage = exportViewModel::clearMessage,
            )
        }
        composable(
            route = ScanoraDestinations.Ocr,
            arguments = listOf(
                navArgument("scanId") { type = NavType.StringType },
                navArgument("pageId") { type = NavType.StringType },
            ),
        ) { entry ->
            val scanId = entry.arguments?.getString("scanId").orEmpty()
            val pageId = entry.arguments?.getString("pageId").orEmpty()
            val ocrViewModel: OcrViewModel = featureViewModel(key = "ocr-$scanId-$pageId") {
                OcrViewModel(
                    scanId = scanId,
                    pageId = pageId,
                    scanRepository = container.scanRepository,
                    processingRepository = container.documentProcessingRepository,
                    ocrRepository = container.ocrRepository,
                )
            }
            val state = ocrViewModel.uiState.collectAsStateWithLifecycle()
            OcrScreen(
                state = state.value,
                onRecognizeAgain = ocrViewModel::recognize,
                onBack = { navController.popBackStack() },
                onClearMessage = ocrViewModel::clearMessage,
            )
        }
        composable(ScanoraDestinations.Settings) {
            val settingsViewModel: SettingsViewModel = featureViewModel {
                SettingsViewModel(container.userPreferencesRepository)
            }
            val state = settingsViewModel.uiState.collectAsStateWithLifecycle()
            SettingsScreen(
                state = state.value,
                onThemeSelected = settingsViewModel::setTheme,
                onDefaultModeSelected = settingsViewModel::setDefaultMode,
                onPdfQualitySelected = settingsViewModel::setPdfQuality,
                onResetOnboarding = settingsViewModel::resetOnboarding,
                onOpenAbout = { navController.navigate(ScanoraDestinations.About) },
            )
        }
        composable(ScanoraDestinations.About) {
            AboutScreen(
                onOpenPrivacyPolicy = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/Soturine/scanora/blob/main/PRIVACY_POLICY.md"),
                    )
                    context.startActivity(intent)
                },
            )
        }
    }
}

private suspend fun createDraftScan(
    container: AppContainer,
    mode: ScanMode,
    uris: List<String>,
): Pair<String, String>? {
    if (uris.isEmpty()) return null
    val formatter = SimpleDateFormat("dd MMM yyyy HH:mm", Locale("pt", "BR"))
    val title = "Scan ${formatter.format(Date())}"
    val scanId = container.scanRepository.createScan(
        title = title,
        mode = mode,
        sourceUris = uris,
    )
    val firstPageId = container.scanRepository.getScan(scanId)?.pages?.minByOrNull { it.index }?.id ?: return null
    return scanId to firstPageId
}

private fun shareFiles(
    context: android.content.Context,
    files: List<ExportedFile>,
) {
    if (files.isEmpty()) return
    val uris = files.map { Uri.parse(it.uri) }
    val intent = if (uris.size == 1) {
        Intent(Intent.ACTION_SEND).apply {
            type = files.first().mimeType
            putExtra(Intent.EXTRA_STREAM, uris.first())
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    } else {
        Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = files.first().mimeType
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
    context.startActivity(Intent.createChooser(intent, "Compartilhar exportação"))
}

private fun openExportedFile(
    context: android.content.Context,
    file: ExportedFile,
) {
    val uri = Uri.parse(file.uri)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, file.mimeType)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val packageManager = context.packageManager
    if (intent.resolveActivity(packageManager) != null) {
        context.startActivity(intent)
    } else {
        shareFiles(context, listOf(file))
    }
}

@Composable
private inline fun <reified T : ViewModel> featureViewModel(
    key: String? = null,
    crossinline create: () -> T,
): T {
    val factory = remember(key) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = create() as VM
        }
    }
    return if (key == null) {
        viewModel(factory = factory)
    } else {
        viewModel(key = key, factory = factory)
    }
}
