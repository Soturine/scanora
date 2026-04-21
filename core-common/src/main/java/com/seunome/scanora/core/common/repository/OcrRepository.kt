package com.seunome.scanora.core.common.repository

interface OcrRepository {
    suspend fun recognizeText(imageUri: String): String
}

