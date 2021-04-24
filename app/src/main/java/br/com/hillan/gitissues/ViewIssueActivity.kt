package br.com.hillan.gitissues

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.hillan.gitissues.models.Issue
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mukesh.MarkdownView
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

class ViewIssueActivity : AppCompatActivity() {


    private lateinit var titleText:   TextView
    private lateinit var dateText:   TextView
    private lateinit var bodyText:    MarkdownView
    private lateinit var imageView:   ImageView

    private lateinit var mMainViewModel: MainViewModel

    private val issueId: Long by lazy {
        intent.getLongExtra("issueId", 0)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_issue)

        setTitle("Issue details")

        mMainViewModel = ViewModelProvider.AndroidViewModelFactory(getApplication()).create(
            MainViewModel::class.java
        )
        mMainViewModel.getIssueByID(issueId).observe(this, {

            configureView(it)

        })



    }

    private fun configureView(it: Issue) {

        titleText = findViewById(R.id.view_issue_title)
        bodyText  = findViewById(R.id.view_issue_body)
        imageView = findViewById(R.id.avatar_imageview)
        dateText  = findViewById(R.id.date_textView)

        titleText.setText(it.title)


        val f: Format = SimpleDateFormat("dd/MM/yy")
        val strDate: String = f.format(it.createdAt)
        dateText.setText(strDate)

        bodyText.setMarkDownText(it.body)

        Glide.with(this).load(it.user.avatarUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .override(120, 120)
            .transform(CircleCrop())
            .into(imageView)

    }
}