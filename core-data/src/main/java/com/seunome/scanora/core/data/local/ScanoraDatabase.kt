package com.seunome.scanora.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seunome.scanora.core.data.local.dao.ScanDao
import com.seunome.scanora.core.data.local.entity.PageEntity
import com.seunome.scanora.core.data.local.entity.ScanEntity

@Database(
    entities = [
        ScanEntity::class,
        PageEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class ScanoraDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao
}

