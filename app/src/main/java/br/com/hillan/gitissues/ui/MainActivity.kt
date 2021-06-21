package br.com.hillan.gitissues.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.hillan.gitissues.IssueViewModel
import br.com.hillan.gitissues.R
import br.com.hillan.gitissues.adapter.IssueListAdapter
import br.com.hillan.gitissues.models.Issue
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    //Lazy Inject ViewModel Koin
    private val mIssueViewModel: IssueViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("Git Issues List")

        mIssueViewModel.allIssues.observe(this,{

            if(it != null) {
                configureRecyclerView(IssueListAdapter(it, this))
            }
        })

//        GlobalScope.launch(Dispatchers.Default) {
//            delay(20000)
//            sendNotification("20 seconds notification")
//        }

    }

    private fun configureRecyclerView(adapter: IssueListAdapter) {
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

    private fun sendNotification(contentText: String) {
        var builder = NotificationCompat.Builder(this, "GITISS01")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("New Issue")
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }

}

