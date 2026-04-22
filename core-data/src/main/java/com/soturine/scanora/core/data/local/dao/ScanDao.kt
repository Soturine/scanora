package com.soturine.scanora.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.soturine.scanora.core.data.local.ScanWithPages
import com.soturine.scanora.core.data.local.entity.PageEntity
import com.soturine.scanora.core.data.local.entity.ScanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDao {
    @Transaction
    @Query(
        """
        SELECT * FROM scans
        WHERE title LIKE '%' || :query || '%'
            OR tags LIKE '%' || :query || '%'
            OR :query = ''
        ORDER BY updatedAt DESC
        """,
    )
    fun observeScans(query: String): Flow<List<ScanWithPages>>

    @Transaction
    @Query("SELECT * FROM scans WHERE id = :scanId")
    fun observeScan(scanId: String): Flow<ScanWithPages?>

    @Transaction
    @Query("SELECT * FROM scans WHERE id = :scanId")
    suspend fun getScanWithPages(scanId: String): ScanWithPages?

    @Query("SELECT * FROM scans WHERE id = :scanId")
    suspend fun getScanEntity(scanId: String): ScanEntity?

    @Query("SELECT * FROM pages WHERE scanId = :scanId ORDER BY pageIndex ASC")
    suspend fun getPages(scanId: String): List<PageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertScan(scan: ScanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPages(pages: List<PageEntity>)

    @Update
    suspend fun updatePage(page: PageEntity)

    @Query("DELETE FROM pages WHERE id = :pageId")
    suspend fun deletePage(pageId: String)

    @Query("DELETE FROM scans WHERE id = :scanId")
    suspend fun deleteScan(scanId: String)

    @Query("UPDATE scans SET updatedAt = :updatedAt WHERE id = :scanId")
    suspend fun touchScan(scanId: String, updatedAt: Long)
}

