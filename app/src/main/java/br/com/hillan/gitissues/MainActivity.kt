package br.com.hillan.gitissues

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.hillan.gitissues.adapter.IssueListAdapter
import br.com.hillan.gitissues.models.Issue
import br.com.hillan.gitissues.services.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var mMainViewModel: MainViewModel

    private var adapter: IssueListAdapter = IssueListAdapter(listOf<Issue>(),this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("Git Issues List")


        mMainViewModel = ViewModelProvider.AndroidViewModelFactory(getApplication()).create(MainViewModel::class.java)
        mMainViewModel.allIssues.observe(this,{

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

