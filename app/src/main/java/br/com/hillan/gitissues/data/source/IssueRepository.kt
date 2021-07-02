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
import br.com.hillan.gitissues.data.source.local.IssueDao
import br.com.hillan.gitissues.data.models.Issue
import br.com.hillan.gitissues.data.source.local.GitIssuesDatabase
import br.com.hillan.gitissues.data.source.remote.IssueService
import br.com.hillan.gitissues.data.source.remote.RetrofitProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class IssueRepository(application: Application) {


    private val mIssueDao: IssueDao = GitIssuesDatabase.getInstance(application).issueDao()
    private val mIssueService: IssueService = RetrofitProvider().service
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    companion object {
        var counter: Int = 0

        fun counter(): Int {
            return counter
        }
    }

    init {
        counter++
        updateListToDb()
        Log.i("REPOSITOR_INS", counter.toString())
    }

    fun updateListToDb() {
        val call = mIssueService.list()
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

    val allIssuesFromDb: Flow<List<Issue>> = mIssueDao.getAll()
    val lastIssue: Flow<Issue> = mIssueDao.getLastIssue()

    fun getIssueByID(id: Long): LiveData<Issue> {
        return mIssueDao.findById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getLast(): Issue {
        return mIssueDao.getLastIssue2()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(issue: Issue) {
        mIssueDao.insertAll(issue)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertList(issues: List<Issue>) {
        mIssueDao.insertList(issues)
    }
}