package br.com.hillan.gitissues.application

import android.app.Application
import android.content.res.Configuration
import br.com.hillan.gitissues.adapter.IssueListAdapter
import br.com.hillan.gitissues.database.GitIssuesDatabase
import br.com.hillan.gitissues.repository.IssueRepository
import org.koin.dsl.module.module

class GitIssuesApplication : Application() {
//    val GitIssuesAppModule = module {
//        factory { IssueListAdapter(context = get()) }
//    }

// Using by lazy so the database and the repository are only created when they're needed
// rather than when the application starts
    val database   by lazy { GitIssuesDatabase.getInstance(this) }
    val repository by lazy { database?.let { IssueRepository(it.issueDao()) } }


    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    override fun onCreate() {
        super.onCreate()
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