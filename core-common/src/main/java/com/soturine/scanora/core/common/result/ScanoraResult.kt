package com.soturine.scanora.core.common.result

sealed interface ScanoraResult<out T> {
    data class Success<T>(val value: T) : ScanoraResult<T>
    data class Error(val message: String, val cause: Throwable? = null) : ScanoraResult<Nothing>
}

