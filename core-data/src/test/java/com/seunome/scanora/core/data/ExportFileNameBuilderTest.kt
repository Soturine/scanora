package com.seunome.scanora.core.data

import com.google.common.truth.Truth.assertThat
import com.seunome.scanora.core.common.model.ExportFormat
import com.seunome.scanora.core.data.export.ExportFileNameBuilder
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import org.junit.Test

class ExportFileNameBuilderTest {
    private val builder = ExportFileNameBuilder(
        clock = Clock.fixed(
            Instant.parse("2026-04-21T12:30:45Z"),
            ZoneId.of("America/Sao_Paulo"),
        ),
    )

    @Test
    fun `deve gerar nome base sanitizado`() {
        val fileName = builder.buildBaseName("Contrato João / Abril", ExportFormat.PDF)

        assertThat(fileName).isEqualTo("contrato-jo-o-abril-20260421-093045.pdf")
    }

    @Test
    fun `deve gerar nome de pagina`() {
        val fileName = builder.buildPageName("Apostila Algoritmos", 1, ExportFormat.JPG)

        assertThat(fileName).isEqualTo("apostila-algoritmos-20260421-093045-p02.jpg")
    }
}
