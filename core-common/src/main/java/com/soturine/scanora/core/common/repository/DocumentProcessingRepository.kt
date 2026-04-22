package com.soturine.scanora.core.common.repository

import com.soturine.scanora.core.common.model.DocumentFilterType
import com.soturine.scanora.core.common.model.DocumentQuad

interface DocumentProcessingRepository {
    suspend fun estimateDocumentQuad(imageUri: String): DocumentQuad

    suspend fun renderPreview(
        sourceUri: String,
        filterType: DocumentFilterType,
        quad: DocumentQuad?,
        rotationDegrees: Int,
    ): String

    suspend fun processPage(
        sourceUri: String,
        filterType: DocumentFilterType,
        quad: DocumentQuad?,
        rotationDegrees: Int,
    ): String
}

