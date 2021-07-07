package br.com.hillan.gitissues.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import br.com.hillan.gitissues.BuildConfig
import br.com.hillan.gitissues.workers.UpdateListWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class GitIssuesApplication : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "GitIssue"
            val descriptionText = "GitIssue Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("GITISS01", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setupWorkManager(context: Context) {
        val updateListWorker: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<UpdateListWorker>(
                15,
                TimeUnit.MINUTES
            ).setInitialDelay(30000, TimeUnit.MILLISECONDS).build()

        try {
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "checkUpdate",
                    ExistingPeriodicWorkPolicy.KEEP,
                    updateListWorker
                )
        } catch (e: Exception) {
            Timber.e(e)
        }

    }

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        //Scheduler task with WorkManager PeriodicWorkRequestBuilder KEEP

        setupWorkManager(applicationContext)

        // Configure Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }


    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

}