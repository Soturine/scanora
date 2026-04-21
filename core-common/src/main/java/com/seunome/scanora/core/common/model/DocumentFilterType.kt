package com.seunome.scanora.core.common.model

enum class DocumentFilterType(
    val storageKey: String,
    val title: String,
) {
    ORIGINAL_CORRECTED(
        storageKey = "original_corrected",
        title = "Original corrigido",
    ),
    DOCUMENT_BLACK_WHITE(
        storageKey = "document_bw",
        title = "Documento P&B",
    ),
    DOCUMENT_GRAY(
        storageKey = "document_gray",
        title = "Documento cinza",
    ),
    COLOR_ENHANCED(
        storageKey = "color_enhanced",
        title = "Colorido aprimorado",
    ),
    RECEIPT_HIGH_CONTRAST(
        storageKey = "receipt_high_contrast",
        title = "Recibo / Alto contraste",
    );

    companion object {
        fun fromStorageKey(value: String): DocumentFilterType =
            entries.firstOrNull { it.storageKey == value } ?: ORIGINAL_CORRECTED
    }
}

