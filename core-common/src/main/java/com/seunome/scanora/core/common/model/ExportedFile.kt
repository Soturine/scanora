package com.seunome.scanora.core.common.model

data class ExportedFile(
    val displayName: String,
    val uri: String,
    val mimeType: String,
    val sizeBytes: Long,
)

