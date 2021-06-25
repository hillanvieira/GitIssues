package br.com.hillan.gitissues.repository

import retrofit2.Call
import android.util.Log
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.GlobalScope
import androidx.annotation.WorkerThread
import br.com.hillan.gitissues.dao.IssueDao
import br.com.hillan.gitissues.models.Issue
import br.com.hillan.gitissues.services.IssueService

class IssueRepository(
    private val mIssueDao: IssueDao,
    private val mIssueService: IssueService
) {

    init {
        updateListToDb()
    }

    fun updateListToDb() {
        val call = mIssueService.list()
        call.enqueue(object : Callback<List<Issue>> {
            override fun onResponse(
                call: Call<List<Issue>?>?,
                response: Response<List<Issue>?>?
            ) {
                response?.body()?.let {
                    GlobalScope.launch { insertList(it) }
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