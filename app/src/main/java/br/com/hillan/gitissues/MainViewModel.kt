package br.com.hillan.gitissues

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import br.com.hillan.gitissues.models.Issue
import br.com.hillan.gitissues.models.User
import br.com.hillan.gitissues.repository.IssueRepository
import java.util.*

internal class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var mRepository : IssueRepository
    internal var allIssues: LiveData<List<Issue>>
    init {
        mRepository = IssueRepository(application)
        allIssues = mRepository.allIssues
    }

    fun getIssueByID(id: Long): LiveData<Issue>{
       return mRepository.getIssueByID(id)
    }

    fun insert(issue:Issue){
        mRepository.insert(issue = issue)
    }
}