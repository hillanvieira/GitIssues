package br.com.hillan.gitissues.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.hillan.gitissues.IssueViewModel
import br.com.hillan.gitissues.IssueViewModelFactory
import br.com.hillan.gitissues.R
import br.com.hillan.gitissues.adapter.IssueListAdapter
import br.com.hillan.gitissues.application.GitIssuesApplication
import br.com.hillan.gitissues.dao.IssueDao
import br.com.hillan.gitissues.database.GitIssuesDatabase
import br.com.hillan.gitissues.models.Issue
import br.com.hillan.gitissues.repository.IssueRepository
import br.com.hillan.gitissues.services.RetrofitInitializer
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    //private lateinit var mIssueViewModel: IssueViewModel//

    private val repository: IssueRepository by inject()

    private val mIssueViewModel: IssueViewModel by viewModels {
        //IssueViewModelFactory((application as GitIssuesApplication).repository!!)
        IssueViewModelFactory(repository)
    }

    private var adapter: IssueListAdapter = IssueListAdapter(listOf<Issue>(),this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("Git Issues List")

        val call = RetrofitInitializer().issueService().list()
        call.enqueue(object: Callback<List<Issue>> {
            override fun onResponse(call: Call<List<Issue>?>?,
                                    response: Response<List<Issue>?>?) {
                response?.body()?.let {
                    //GitIssuesDatabase.databaseWriteExecutor.execute {  mIssueDao.insertList(it) }

                    mIssueViewModel.insertList(it)

                    //Log.i("RETROFIT_RESPONSE",it.toString())
                }
            }
            override fun onFailure(call: Call<List<Issue>?>?,
                                   t: Throwable?) {
            }
        })

       // mIssueViewModel = ViewModelProvider.AndroidViewModelFactory(getApplication()).create(IssueViewModel::class.java)
        mIssueViewModel.allIssues.observe(this,{

            adapter = IssueListAdapter(it,this)
            configureRecyclerView()

        })

    }

    private fun configureRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.issue_list)
        recyclerView.adapter = adapter
        adapter.whenClicked = this::openIssueView
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        //val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
    }

    private fun openIssueView(it: Issue) {
        val intent = Intent(this, ViewIssueActivity::class.java)
        intent.putExtra("issueId", it.id)
        startActivity(intent)
    }

}

