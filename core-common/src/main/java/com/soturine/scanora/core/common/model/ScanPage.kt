package com.soturine.scanora.core.common.model

data class ScanPage(
    val id: String,
    val scanId: String,
    val index: Int,
    val sourceUri: String,
    val processedUri: String? = null,
    val filterType: DocumentFilterType = DocumentFilterType.ORIGINAL_CORRECTED,
    val rotationDegrees: Int = 0,
    val quad: DocumentQuad? = null,
    val ocrText: String? = null,
) {
    val displayUri: String
        get() = processedUri ?: sourceUri
}

