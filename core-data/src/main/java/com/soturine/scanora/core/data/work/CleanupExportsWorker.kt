package com.soturine.scanora.core.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.io.File
import java.util.concurrent.TimeUnit

class CleanupExportsWorker(
    appContext: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        val threshold = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(48)
        cleanupDirectory(File(applicationContext.cacheDir, "processed"), threshold)
        cleanupDirectory(
            File(applicationContext.getExternalFilesDir(android.os.Environment.DIRECTORY_DOCUMENTS), "exports"),
            threshold,
        )
        return Result.success()
    }

    private fun cleanupDirectory(
        directory: File?,
        threshold: Long,
    ) {
        if (directory == null || !directory.exists()) return
        directory.listFiles().orEmpty()
            .filter { it.isFile && it.lastModified() < threshold }
            .forEach(File::delete)
    }
}

