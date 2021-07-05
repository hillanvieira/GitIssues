package br.com.hillan.gitissues.data.source

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import br.com.hillan.gitissues.data.models.Issue
import br.com.hillan.gitissues.data.source.local.GitIssuesDatabase
import br.com.hillan.gitissues.data.source.local.IssuesLocalDataSource
import br.com.hillan.gitissues.data.source.remote.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import br.com.hillan.gitissues.data.Result
import br.com.hillan.gitissues.data.Result.Success
import br.com.hillan.gitissues.data.Result.Error



class DefaultIssueRepository(application: Application) {

    private val issuesLocalDataSource: IssuesLocalDataSource
    private val issuesRemoteDataSource: IssuesRemoteDataSource
    //private val mIssueService: IssueService = RetrofitProvider().service
    //private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    companion object {
        @Volatile
        private var INSTANCE: DefaultIssueRepository? = null

        fun getRepository(app: Application): DefaultIssueRepository {
            return INSTANCE ?: synchronized(this) {
                DefaultIssueRepository(app).also {
                    INSTANCE = it
                }
            }
        }


        val FRESH_TIMEOUT = TimeUnit.DAYS.toMillis(1)
        var counter: Int = 0
        fun counter(): Int {
            return counter
        }
    }

    init {

        val database = Room.databaseBuilder(
            application.applicationContext,
            GitIssuesDatabase::class.java, "issue.db")
            .build()
        issuesLocalDataSource = IssuesLocalDataSource(database.issueDao())


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_GITISSUE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IssueService::class.java)
        //val service = retrofit.create(IssueService::class.java)
        issuesRemoteDataSource = IssuesRemoteDataSource(retrofit)

        counter++
        Log.i("INSTANCES_VMODEL",counter.toString())
    }

    suspend fun getIssues(forceUpdate: Boolean = false): Result<List<Issue>> {
        if (forceUpdate) {
            try {
                updateIssuesFromRemoteDataSource()
            } catch (ex: Exception) {
                return Error(ex)
            }
        }
        return issuesLocalDataSource.getIssues()
    }

    suspend fun refreshIssues() {
        updateIssuesFromRemoteDataSource()
    }

    fun observeIssues(): LiveData<Result<List<Issue>>> {
        return issuesLocalDataSource.observeIssues()
    }

    private suspend fun updateIssuesFromRemoteDataSource() {
        val remoteIssues = issuesRemoteDataSource.getIssues()

        if (remoteIssues is Success) {
            // Real apps might want to do a proper sync.
            issuesLocalDataSource.deleteAllIssues()
            remoteIssues.data.forEach { issue ->
                issuesLocalDataSource.saveIssue(issue)
            }
        } else if (remoteIssues is Error) {
            throw remoteIssues.exception
        }
    }

    fun observeIssue(issueId: Long): LiveData<Result<Issue>> {
        return issuesLocalDataSource.observeIssue(issueId)
    }


    suspend fun getIssue(issueId: Long,  forceUpdate: Boolean = false): Result<Issue> {
        if (forceUpdate) {
            updateIssuesFromRemoteDataSource()
        }
        return issuesLocalDataSource.getIssue(issueId)
    }

    private suspend fun getIssueWithId(id: Long): Result<Issue> {
        return issuesLocalDataSource.getIssue(id)
    }

    suspend fun getLastIssue(): Result<Issue>{
        return issuesLocalDataSource.getLastIssue()
    }

}

