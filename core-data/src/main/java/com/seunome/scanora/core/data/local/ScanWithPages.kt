package com.seunome.scanora.core.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.seunome.scanora.core.data.local.entity.PageEntity
import com.seunome.scanora.core.data.local.entity.ScanEntity

data class ScanWithPages(
    @Embedded val scan: ScanEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "scanId",
    )
    val pages: List<PageEntity>,
)

