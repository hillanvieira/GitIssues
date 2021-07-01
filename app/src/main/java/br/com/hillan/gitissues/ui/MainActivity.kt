package br.com.hillan.gitissues.ui

import android.os.Bundle
import br.com.hillan.gitissues.R
import androidx.appcompat.app.AppCompatActivity
import br.com.hillan.gitissues.viewmodel.IssueViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mIssueViewModel: IssueViewModel by viewModel<IssueViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.issues_host)
    }

}

