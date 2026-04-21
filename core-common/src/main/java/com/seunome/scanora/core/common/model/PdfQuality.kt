package com.seunome.scanora.core.common.model

enum class PdfQuality(
    val storageKey: String,
    val title: String,
    val jpegQuality: Int,
) {
    COMPACT(
        storageKey = "compact",
        title = "Compacto",
        jpegQuality = 70,
    ),
    BALANCED(
        storageKey = "balanced",
        title = "Equilibrado",
        jpegQuality = 84,
    ),
    HIGH(
        storageKey = "high",
        title = "Alta qualidade",
        jpegQuality = 95,
    );

    companion object {
        fun fromStorageKey(value: String): PdfQuality =
            entries.firstOrNull { it.storageKey == value } ?: BALANCED
    }
}

