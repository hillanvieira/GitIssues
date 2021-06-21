package br.com.hillan.gitissues.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import br.com.hillan.gitissues.IssueViewModel
import br.com.hillan.gitissues.database.GitIssuesDatabase
import br.com.hillan.gitissues.repository.IssueRepository
import br.com.hillan.gitissues.services.RetrofitInitializer
import br.com.hillan.gitissues.viewmodel.IssueDetailViewModel
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


class GitIssuesApplication : Application() {

    //Koin modules
    private val appModule = module {

        //koin single each injection will use the same instance
        single { GitIssuesDatabase.getInstance(applicationContext)!! }
        single { RetrofitInitializer().provideRetrofit() }

        //koin factory will create a new instance each time the component is injected
        factory {
            IssueRepository(
                get<GitIssuesDatabase>().issueDao(),
                RetrofitInitializer().issueService(get())
            )
        }

        //koin viewModel
        viewModel { IssueViewModel(this.androidApplication() ,get()) }
        viewModel { IssueDetailViewModel(get()) }

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

        // Required initialization logic here!
        createNotificationChannel()

    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    override fun onLowMemory() {
        super.onLowMemory()
    }

}