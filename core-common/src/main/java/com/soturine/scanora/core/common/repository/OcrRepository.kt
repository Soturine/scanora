package com.soturine.scanora.core.common.repository

import com.soturine.scanora.core.common.model.OcrTextResult

interface OcrRepository {
    suspend fun recognizeText(imageUri: String): OcrTextResult
}

