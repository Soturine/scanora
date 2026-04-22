package com.soturine.scanora.core.common

import com.google.common.truth.Truth.assertThat
import com.soturine.scanora.core.common.usecase.FormatScanDateUseCase
import org.junit.Test

class FormatScanDateUseCaseTest {
    private val useCase = FormatScanDateUseCase()

    @Test
    fun `deve formatar data em pt br`() {
        val formatted = useCase(1_775_743_200_000L)

        assertThat(formatted).isEqualTo("09 abr 2026, 11:00")
    }
}
