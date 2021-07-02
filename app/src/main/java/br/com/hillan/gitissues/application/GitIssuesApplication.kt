package br.com.hillan.gitissues.application

import android.os.Build
import android.app.Application
import android.content.Context
import org.koin.dsl.module.module
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import br.com.hillan.gitissues.viewmodel.IssueViewModel
import org.koin.android.ext.android.startKoin
import org.koin.android.viewmodel.ext.koin.viewModel
import br.com.hillan.gitissues.data.source.local.GitIssuesDatabase
import br.com.hillan.gitissues.util.UpdateListWorker
import java.util.concurrent.TimeUnit


class GitIssuesApplication : Application() {

    //Koin modules
    private val appModule = module {

        //koin single each injection will use the same instance
        single { GitIssuesDatabase.getInstance(applicationContext) } //notUsed

        //koin factory will create a new instance each time the component is injected

        //koin viewModel
        viewModel { IssueViewModel(get()) }

    }

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

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin(this, listOf(appModule))

        createNotificationChannel()

    }

}