package com.soturine.scanora.core.common.result

data class ValidationResult(
    val isValid: Boolean,
    val sanitizedValue: String,
    val errorMessage: String? = null,
)

