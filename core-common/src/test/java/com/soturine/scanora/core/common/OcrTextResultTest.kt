package com.soturine.scanora.core.common

import com.google.common.truth.Truth.assertThat
import com.soturine.scanora.core.common.model.OcrTextResult
import org.junit.Test

class OcrTextResultTest {
    @Test
    fun `deve montar blocos a partir de texto salvo`() {
        val result = OcrTextResult.fromPlainText("Linha 1\nLinha 2\n\nOutro bloco")

        assertThat(result.blocks).hasSize(2)
        assertThat(result.blocks.first().lines.map { it.text }).containsExactly("Linha 1", "Linha 2")
        assertThat(result.fullText).isEqualTo("Linha 1\nLinha 2\n\nOutro bloco")
    }
}
