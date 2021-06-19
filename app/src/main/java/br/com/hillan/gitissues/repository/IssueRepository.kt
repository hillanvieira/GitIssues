package br.com.hillan.gitissues.repository


import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import br.com.hillan.gitissues.dao.IssueDao
import br.com.hillan.gitissues.models.Issue
import br.com.hillan.gitissues.services.RetrofitInitializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IssueRepository(
    private val mIssueDao: IssueDao
) {

    init {
        val call = RetrofitInitializer().issueService().list()
        call.enqueue(object: Callback<List<Issue>> {
            override fun onResponse(call: Call<List<Issue>?>?,
                                    response: Response<List<Issue>?>?) {
                response?.body()?.let {

                GlobalScope.launch { insertList(it)  }

                    //Log.i("RETROFIT_RESPONSE",it.toString())
                }
            }
            override fun onFailure(call: Call<List<Issue>?>?,
                                   t: Throwable?) {
            }
        })
    }

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