package com.seunome.scanora.feature.camera

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraCaptureScreen(
    state: CameraCaptureUiState,
    onPermissionResult: (Boolean) -> Unit,
    onCapturedImage: (String) -> Unit,
    onCapturedBatch: (List<String>) -> Unit,
    onBack: () -> Unit,
    onCaptureStarted: () -> Unit,
    onCaptureFinished: () -> Unit,
    onError: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scannerUnavailableMessage = stringResource(id = R.string.camera_scanner_unavailable)
    val captureFailedMessage = stringResource(id = R.string.camera_capture_failed)
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = onPermissionResult,
    )
    val guidedScanLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
    ) { result ->
        val scanResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)
        val pages = scanResult?.pages.orEmpty().mapNotNull { it.imageUri?.toString() }
        if (pages.isNotEmpty()) {
            onCapturedBatch(pages)
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = state.mode.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = stringResource(id = R.string.camera_back),
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (activity == null) {
                                onError(scannerUnavailableMessage)
                                return@IconButton
                            }
                            val options = GmsDocumentScannerOptions.Builder()
                                .setGalleryImportAllowed(true)
                                .setPageLimit(12)
                                .setResultFormats(
                                    GmsDocumentScannerOptions.RESULT_FORMAT_JPEG,
                                    GmsDocumentScannerOptions.RESULT_FORMAT_PDF,
                                )
                                .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
                                .build()
                            GmsDocumentScanning.getClient(options)
                                .getStartScanIntent(activity)
                                .addOnSuccessListener { intentSender ->
                                    guidedScanLauncher.launch(
                                        IntentSenderRequest.Builder(intentSender).build(),
                                    )
                                }
                                .addOnFailureListener {
                                    onError(scannerUnavailableMessage)
                                }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DocumentScanner,
                            contentDescription = stringResource(id = R.string.camera_guided_scan),
                        )
                    }
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        if (state.permissionGranted) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                CameraPreview(
                    imageCapture = imageCapture,
                    lifecycleOwner = lifecycleOwner,
                    modifier = Modifier.fillMaxSize(),
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FilledTonalIconButton(
                        modifier = Modifier.size(88.dp),
                        onClick = {
                            onCaptureStarted()
                            capturePhoto(
                                context = context,
                                imageCapture = imageCapture,
                                onCaptured = {
                                    onCaptureFinished()
                                    onCapturedImage(it)
                                },
                                onFailure = {
                                    onError(captureFailedMessage)
                                },
                            )
                        },
                    ) {
                        if (state.isCapturing) {
                            CircularProgressIndicator()
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.CameraAlt,
                                contentDescription = stringResource(id = R.string.camera_take_photo),
                            )
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.camera_permission_title),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(id = R.string.camera_permission_message),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Button(
                    modifier = Modifier.padding(top = 20.dp),
                    onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                ) {
                    Text(text = stringResource(id = R.string.camera_permission_action))
                }
            }
        }
    }
}

@Composable
private fun CameraPreview(
    imageCapture: ImageCapture,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(lifecycleOwner, imageCapture) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(previewView.surfaceProvider)
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            imageCapture,
        )
    }

    AndroidView(
        modifier = modifier.background(Color.Black),
        factory = { previewView },
    )
}

private fun capturePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onCaptured: (String) -> Unit,
    onFailure: () -> Unit,
) {
    val outputFile = File(context.cacheDir, "capture-${System.currentTimeMillis()}.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onCaptured(Uri.fromFile(outputFile).toString())
            }

            override fun onError(exception: ImageCaptureException) {
                onFailure()
            }
        },
    )
}

private fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
