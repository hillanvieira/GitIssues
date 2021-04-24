package br.com.hillan.gitissues.repository
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import br.com.hillan.gitissues.adapter.IssueListAdapter
import br.com.hillan.gitissues.dao.IssueDao
import br.com.hillan.gitissues.database.GitIssuesDatabase
import br.com.hillan.gitissues.models.Issue
import br.com.hillan.gitissues.services.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IssueRepository(
    application: Application
) {
    private lateinit var mIssueDao: IssueDao

    var allIssues: LiveData<List<Issue>>

    init {
        val db = GitIssuesDatabase.getInstance(application)

        if (db != null) {
            mIssueDao = db.IssueDao()
        }


        val call = RetrofitInitializer().issueService().list()
        call.enqueue(object: Callback<List<Issue>> {
            override fun onResponse(call: Call<List<Issue>?>?,
                                    response: Response<List<Issue>?>?) {
                response?.body()?.let {
                    //val issues = it
                    GitIssuesDatabase.databaseWriteExecutor.execute {  mIssueDao.insertList(it) }

                    Log.i("RETROFIT_RESPONSE",it.toString())
                }
            }
            override fun onFailure(call: Call<List<Issue>?>?,
                                   t: Throwable?) {
            }
        })

        allIssues = mIssueDao.getAll()

    }

    fun getIssueByID(id: Long): LiveData<Issue>{
        return mIssueDao.findById(id)
    }


    fun insert(issue: Issue) {
        GitIssuesDatabase.databaseWriteExecutor.execute { mIssueDao.insertAll(issue) }
    }

}