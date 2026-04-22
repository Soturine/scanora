package com.soturine.scanora

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.soturine.scanora.app.AppContainer
import com.soturine.scanora.core.data.work.CleanupExportsWorker
import java.util.concurrent.TimeUnit

class ScanoraApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "scanora_cleanup_exports",
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<CleanupExportsWorker>(12, TimeUnit.HOURS).build(),
        )
    }
}

