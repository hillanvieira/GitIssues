package br.com.hillan.gitissues.application

import android.app.Application
import android.content.res.Configuration
import br.com.hillan.gitissues.IssueViewModel
import br.com.hillan.gitissues.database.GitIssuesDatabase
import br.com.hillan.gitissues.repository.IssueRepository
import org.koin.android.ext.android.startKoin
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

class GitIssuesApplication : Application() {

// Using by lazy so the database and the repository are only created when they're needed
// rather than when the application starts

   private val appModule = module {
       //koin single each injection will use the same instance
        single {GitIssuesDatabase.getInstance(applicationContext)!!}
       //koin factory will create a new instance each time the component is injected
        factory { IssueRepository(get<GitIssuesDatabase>().issueDao()) }
       //Koin Scoped we can stop the instances

       //koin viewModel
       viewModel {IssueViewModel(get())}
    }

   // val database   by lazy { GitIssuesDatabase.getInstance(this) }
   // val repository by lazy { database?.let { IssueRepository(it.issueDao()) } }


    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin(this, listOf(appModule))

        // Required initialization logic here!
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    override fun onConfigurationChanged ( newConfig : Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    override fun onLowMemory() {
        super.onLowMemory()
    }

}