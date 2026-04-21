package com.seunome.scanora.app

import android.content.Context
import androidx.room.Room
import com.seunome.scanora.core.common.repository.DocumentProcessingRepository
import com.seunome.scanora.core.common.repository.ExportRepository
import com.seunome.scanora.core.common.repository.OcrRepository
import com.seunome.scanora.core.common.repository.ScanRepository
import com.seunome.scanora.core.common.repository.UserPreferencesRepository
import com.seunome.scanora.core.data.datastore.DefaultUserPreferencesRepository
import com.seunome.scanora.core.data.export.DefaultExportRepository
import com.seunome.scanora.core.data.image.DefaultDocumentProcessingRepository
import com.seunome.scanora.core.data.local.ScanoraDatabase
import com.seunome.scanora.core.data.ocr.DefaultOcrRepository
import com.seunome.scanora.core.data.repository.DefaultScanRepository

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

    val exportRepository: ExportRepository by lazy {
        DefaultExportRepository(context)
    }

    val documentProcessingRepository: DocumentProcessingRepository by lazy {
        DefaultDocumentProcessingRepository(context)
    }

    val ocrRepository: OcrRepository by lazy {
        DefaultOcrRepository(context)
    }
}

