package br.com.hillan.gitissues.ui

import android.os.Bundle
import androidx.activity.viewModels
import br.com.hillan.gitissues.R
import androidx.appcompat.app.AppCompatActivity
import br.com.hillan.gitissues.viewmodel.IssueViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mIssueViewModel: IssueViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.issues_host)
    }

}

