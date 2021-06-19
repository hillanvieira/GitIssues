package br.com.hillan.gitissues

import androidx.lifecycle.*
import br.com.hillan.gitissues.models.Issue
import br.com.hillan.gitissues.repository.IssueRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IssueViewModel(
    private val mRepository: IssueRepository
) : ViewModel() {

    val allIssues: LiveData<List<Issue>> = mRepository.allIssues.asLiveData()

    fun getIssueByID(id: Long): LiveData<Issue>{
       return mRepository.getIssueByID(id)
    }

    fun insert(issue:Issue) = viewModelScope.launch(Dispatchers.IO){
        mRepository.insert(issue = issue)
    }

    fun insertList(issues : List<Issue>) = viewModelScope.launch(Dispatchers.IO){
        mRepository.insertList(issues = issues)
    }

}