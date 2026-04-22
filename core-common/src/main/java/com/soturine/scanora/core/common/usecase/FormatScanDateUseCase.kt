package com.soturine.scanora.core.common.usecase

import com.soturine.scanora.core.common.util.DateFormatter

class FormatScanDateUseCase(
    private val dateFormatter: DateFormatter = DateFormatter(),
) {
    operator fun invoke(timestamp: Long): String = dateFormatter.format(timestamp)
}

