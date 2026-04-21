package com.seunome.scanora.core.common.model

enum class ScanMode(
    val storageKey: String,
    val title: String,
    val subtitle: String,
) {
    NOTEBOOK(
        storageKey = "notebook",
        title = "Caderno / Faculdade",
        subtitle = "Pensado para apostilas, quadros e páginas de estudo.",
    ),
    DOCUMENT(
        storageKey = "document",
        title = "Documento / Contrato",
        subtitle = "Foco em legibilidade, alinhamento e PDF limpo.",
    ),
    RECEIPT(
        storageKey = "receipt",
        title = "Recibo / Nota",
        subtitle = "Ajusta contraste para comprovantes e papéis térmicos.",
    );

    companion object {
        fun fromStorageKey(value: String): ScanMode = entries.firstOrNull { it.storageKey == value } ?: DOCUMENT
    }
}

