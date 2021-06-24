package br.com.hillan.gitissues.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.hillan.gitissues.IssueViewModel
import br.com.hillan.gitissues.R
import br.com.hillan.gitissues.databinding.ActivityMainBinding
import br.com.hillan.gitissues.databinding.ActivityViewIssueBinding
import br.com.hillan.gitissues.models.Issue
import br.com.hillan.gitissues.viewmodel.IssueDetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mukesh.MarkdownView
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.Format
import java.text.SimpleDateFormat

class ViewIssueActivity : AppCompatActivity() {

    private lateinit var titleText:   TextView
    private lateinit var dateText:   TextView
    private lateinit var bodyText:    MarkdownView
    private lateinit var imageView:   ImageView

    //Lazy Inject ViewModel Koin
    private val mIssueDetailViewModel: IssueDetailViewModel by viewModel()
    private lateinit var binding: ActivityViewIssueBinding

    private val issueId: Long by lazy {
        intent.getLongExtra("issueId", 0)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_view_issue) sem binding
        binding = ActivityViewIssueBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setTitle("Issue details")

       // mIssueViewModel = ViewModelProvider.AndroidViewModelFactory(getApplication()).create(IssueViewModel::class.java)
        mIssueDetailViewModel.getIssue(issueId).observe(this, {

            configureView(it)

        })

    }

    private fun configureView(it: Issue) {

//        titleText = findViewById(R.id.view_issue_title) sem binding
//        bodyText  = findViewById(R.id.view_issue_body)  sem binding
//        imageView = findViewById(R.id.avatar_imageview) sem binding
//        dateText  = findViewById(R.id.date_textView)    sem binding

        titleText = binding.viewIssueTitle
        bodyText  = binding.viewIssueBody
        imageView = binding.avatarImageview
        dateText  = binding.dateTextView

        titleText.text = it.title


        val f: Format = SimpleDateFormat("dd/MM/yy")
        val strDate: String = f.format(it.createdAt)
        dateText.text = strDate

        bodyText.setMarkDownText(it.body)

        Glide.with(this).load(it.user.avatarUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .override(120, 120)
            .transform(CircleCrop())
            .into(imageView)

    }
}