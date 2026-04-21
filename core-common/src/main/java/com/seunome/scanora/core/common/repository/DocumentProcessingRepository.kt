package com.seunome.scanora.core.common.repository

import com.seunome.scanora.core.common.model.DocumentFilterType
import com.seunome.scanora.core.common.model.DocumentQuad

interface DocumentProcessingRepository {
    suspend fun estimateDocumentQuad(imageUri: String): DocumentQuad

    suspend fun processPage(
        sourceUri: String,
        filterType: DocumentFilterType,
        quad: DocumentQuad?,
        rotationDegrees: Int,
    ): String
}

