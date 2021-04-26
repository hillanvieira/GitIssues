package br.com.hillan.gitissues.repository


import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import br.com.hillan.gitissues.dao.IssueDao
import br.com.hillan.gitissues.database.GitIssuesDatabase
import br.com.hillan.gitissues.models.Issue
import br.com.hillan.gitissues.services.RetrofitInitializer
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IssueRepository(
    private val mIssueDao: IssueDao
) {
    //private lateinit var mIssueDao: IssueDao

    val allIssues: Flow<List<Issue>> = mIssueDao.getAll()

    fun getIssueByID(id: Long): LiveData<Issue>{
        return mIssueDao.findById(id)
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