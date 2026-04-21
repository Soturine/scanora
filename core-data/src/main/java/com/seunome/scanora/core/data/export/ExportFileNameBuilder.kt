package com.seunome.scanora.core.data.export

import com.seunome.scanora.core.common.model.ExportFormat
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class ExportFileNameBuilder(
    private val clock: Clock = Clock.system(ZoneId.of("America/Sao_Paulo")),
) {
    fun buildBaseName(
        title: String,
        format: ExportFormat,
        suffix: String? = null,
    ): String {
        val slug = normalize(title)
        val stamp = formatter.format(Instant.now(clock))
        val optionalSuffix = suffix?.let { "-$it" }.orEmpty()
        return "$slug-$stamp$optionalSuffix.${format.fileExtension}"
    }

    fun buildPageName(
        title: String,
        pageIndex: Int,
        format: ExportFormat,
    ): String = buildBaseName(
        title = title,
        format = format,
        suffix = "p${(pageIndex + 1).toString().padStart(2, '0')}",
    )

    private fun normalize(title: String): String =
        title
            .trim()
            .ifBlank { "scanora-scan" }
            .lowercase(Locale.ROOT)
            .replace(Regex("[^a-z0-9]+"), "-")
            .trim('-')
            .take(50)
            .ifBlank { "scanora-scan" }

    private companion object {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
            "yyyyMMdd-HHmmss",
            Locale.ROOT,
        ).withZone(ZoneId.of("America/Sao_Paulo"))
    }
}

