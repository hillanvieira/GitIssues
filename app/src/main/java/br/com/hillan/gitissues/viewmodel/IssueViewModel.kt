package br.com.hillan.gitissues

import androidx.lifecycle.*
import br.com.hillan.gitissues.models.Issue
import br.com.hillan.gitissues.repository.IssueRepository
import kotlinx.coroutines.launch

class IssueViewModel(
    private val mRepository: IssueRepository
) : ViewModel() {

    val allIssues: LiveData<List<Issue>> = mRepository.allIssues.asLiveData()


    fun getIssueByID(id: Long): LiveData<Issue>{
       return mRepository.getIssueByID(id)
    }

    fun insert(issue:Issue) = viewModelScope.launch{
        mRepository.insert(issue = issue)
    }
}

class IssueViewModelFactory(private val mRepository: IssueRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IssueViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IssueViewModel(mRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}