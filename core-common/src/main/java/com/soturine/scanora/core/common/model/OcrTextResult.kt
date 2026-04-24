package com.soturine.scanora.core.common.model

data class OcrTextLine(
    val text: String,
)

data class OcrTextBlock(
    val lines: List<OcrTextLine>,
) {
    val text: String
        get() = lines.joinToString(separator = "\n") { it.text }.trim()
}

data class OcrTextResult(
    val blocks: List<OcrTextBlock>,
    val fallbackText: String = "",
) {
    val fullText: String
        get() = blocks.joinToString(separator = "\n\n") { it.text }
            .ifBlank { fallbackText.trim() }

    companion object {
        val Empty = OcrTextResult(blocks = emptyList())

        fun fromPlainText(text: String): OcrTextResult {
            val blocks = text
                .split(Regex("\\n{2,}"))
                .mapNotNull { rawBlock ->
                    val lines = rawBlock
                        .lines()
                        .map(String::trim)
                        .filter(String::isNotBlank)
                        .map(::OcrTextLine)
                    lines.takeIf { it.isNotEmpty() }?.let(::OcrTextBlock)
                }
            return OcrTextResult(
                blocks = blocks,
                fallbackText = text,
            )
        }
    }
}
