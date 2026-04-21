package com.seunome.scanora.core.common.usecase

import com.seunome.scanora.core.common.util.DateFormatter

class FormatScanDateUseCase(
    private val dateFormatter: DateFormatter = DateFormatter(),
) {
    operator fun invoke(timestamp: Long): String = dateFormatter.format(timestamp)
}

