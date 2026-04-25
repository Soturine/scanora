package com.soturine.scanora.app

import android.content.Context
import androidx.room.Room
import com.soturine.scanora.core.common.repository.DocumentProcessingRepository
import com.soturine.scanora.core.common.repository.ExportRepository
import com.soturine.scanora.core.common.repository.OcrRepository
import com.soturine.scanora.core.common.repository.ScanRepository
import com.soturine.scanora.core.common.repository.UserPreferencesRepository
import com.soturine.scanora.core.data.datastore.DefaultUserPreferencesRepository
import com.soturine.scanora.core.data.export.DefaultExportRepository
import com.soturine.scanora.core.data.image.DefaultDocumentProcessingRepository
import com.soturine.scanora.core.data.local.ScanoraDatabase
import com.soturine.scanora.core.data.ocr.DefaultOcrRepository
import com.soturine.scanora.core.data.repository.DefaultScanRepository

class AppContainer(
    private val context: Context,
) {
    private val database: ScanoraDatabase by lazy {
        Room.databaseBuilder(
            context,
            ScanoraDatabase::class.java,
            "scanora.db",
        ).fallbackToDestructiveMigration().build()
    }

    val scanRepository: ScanRepository by lazy {
        DefaultScanRepository(database.scanDao())
    }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        DefaultUserPreferencesRepository(context)
    }

    val documentProcessingRepository: DocumentProcessingRepository by lazy {
        DefaultDocumentProcessingRepository(context)
    }

    val exportRepository: ExportRepository by lazy {
        DefaultExportRepository(
            context = context,
            processingRepository = documentProcessingRepository,
        )
    }

    val ocrRepository: OcrRepository by lazy {
        DefaultOcrRepository(context)
    }
}

