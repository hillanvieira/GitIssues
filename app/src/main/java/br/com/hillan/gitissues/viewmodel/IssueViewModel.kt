package br.com.hillan.gitissues.viewmodel

import androidx.lifecycle.*
import android.app.Application
import br.com.hillan.gitissues.data.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import br.com.hillan.gitissues.data.models.Issue
import br.com.hillan.gitissues.data.models.User
import br.com.hillan.gitissues.data.source.DefaultIssueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher

import java.util.*
import javax.inject.Inject

@HiltViewModel
class IssueViewModel @Inject constructor(application: Application, val issueRepository: DefaultIssueRepository) : AndroidViewModel(application) {


    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val _forceUpdate = MutableLiveData<Boolean>(false)
    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading


    private val _items: LiveData<List<Issue>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                issueRepository.refreshIssues()
                _dataLoading.value = false
            }
        }
        issueRepository.observeIssues().switchMap { it ->

            val result = MutableLiveData<List<Issue>>()
            if (it is Result.Success) {
                result.value = it.data
            } else {
                result.value = emptyList()
            }
            result
        }
    }

    val items: LiveData<List<Issue>> = _items

    val idInput = MutableLiveData<Long>()
    val issueById: LiveData<Issue> = Transformations.switchMap(idInput) { it ->
        issueRepository.observeIssue(it).switchMap { it ->

            val result = MutableLiveData<Issue>()
            if (it is Result.Success) {
                result.value = it.data
            } else {
                result.value = Issue(0L, "Error", Date(), "#Error", "", "", User(""))
            }
            result
        }
    }

    init {

        viewModelScope.launch {
            issueRepository.refreshIssues()
        }

        //testes with coroutines
//        GlobalScope.launch(Dispatchers.IO) {
//            delay(2000)
//            allIssues.postValue(emptyList())
//            delay(20000)
//            mRepository.allIssuesFromDb.collect { list -> allIssues.postValue(list) }
//        }

    }
}