package br.com.hillan.gitissues.data.source

import android.app.Application
import retrofit2.Call
import android.util.Log
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.GlobalScope
import androidx.annotation.WorkerThread
import androidx.room.Room
import br.com.hillan.gitissues.data.models.Issue
import br.com.hillan.gitissues.data.source.local.GitIssuesDatabase
import br.com.hillan.gitissues.data.source.local.IssuesLocalDataSource
import br.com.hillan.gitissues.data.source.remote.IssueService
import br.com.hillan.gitissues.data.source.remote.RetrofitProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

class DefaultIssueRepository(application: Application) {

    private val issuesLocalDataSource: IssuesLocalDataSource
    private val mIssueService: IssueService = RetrofitProvider().service
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

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

        val database = Room.databaseBuilder(application.applicationContext,
            GitIssuesDatabase::class.java, "issue.db")
            .build()
        issuesLocalDataSource = IssuesLocalDataSource(database.issueDao())

        counter++
        updateListToDb()
        Log.i("REPOSITOR_INS", counter.toString())

    }

    fun updateListToDb() {


        val call = mIssueService.getIssues()
        call.enqueue(object : Callback<List<Issue>> {
            override fun onResponse(
                call: Call<List<Issue>?>?,
                response: Response<List<Issue>?>?
            ) {
                response?.body()?.let {
                    GlobalScope.launch(ioDispatcher) { insertList(it) }
                    //Log.i("RETROFIT_RESPONSE",it.toString())
                }
            }

            override fun onFailure(
                call: Call<List<Issue>?>?,
                t: Throwable?
            ) {
                Log.i("RETROFIT_RESPONSE", t.toString())
            }
        })
    }

    val allIssuesFromDb: Flow<List<Issue>>
    get() {
        updateListToDb()

        return mIssueDao.observeIssues()
    }



    val lastIssue: Flow<Issue> = mIssueDao.observeLastIssue()

    fun getIssueByID(id: Long): LiveData<Issue> {
        return mIssueDao.observeIssueById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getLast(): Issue {
        return mIssueDao.getLastIssue()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(issue: Issue) {
        mIssueDao.insertIssues(issue)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertList(issues: List<Issue>) {
        mIssueDao.insertListIssues(issues)
    }

}

