package br.com.hillan.gitissues.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.hillan.gitissues.models.Issue
import br.com.hillan.gitissues.repository.IssueRepository

class IssueDetailViewModel(
    private val mRepository: IssueRepository
) : ViewModel() {

    fun getIssue(id: Long): LiveData<Issue> {
        val issue: LiveData<Issue> = mRepository.getIssueByID(id)
        return issue
    }

}