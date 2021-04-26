package br.com.hillan.gitissues.application

import android.app.Application
import android.content.res.Configuration
import br.com.hillan.gitissues.adapter.IssueListAdapter
import br.com.hillan.gitissues.dao.IssueDao
import br.com.hillan.gitissues.database.GitIssuesDatabase
import br.com.hillan.gitissues.repository.IssueRepository
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module

class GitIssuesApplication : Application() {

// Using by lazy so the database and the repository are only created when they're needed
// rather than when the application starts

   private val repositoryModule = module {
        single {GitIssuesDatabase.getInstance(applicationContext)!!}
        factory { IssueRepository(get<GitIssuesDatabase>().issueDao()) }
    }

   // val database   by lazy { GitIssuesDatabase.getInstance(this) }
   // val repository by lazy { database?.let { IssueRepository(it.issueDao()) } }


    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(repositoryModule))

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