package br.com.hillan.gitissues.viewmodel

import androidx.work.*
import androidx.lifecycle.*
import android.app.Application
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import br.com.hillan.gitissues.data.models.Issue
import br.com.hillan.gitissues.util.UpdateListWorker
import br.com.hillan.gitissues.data.source.DefaultIssueRepository
import kotlinx.coroutines.CoroutineDispatcher


class IssueViewModel(application: Application) : AndroidViewModel(application) {

    private val issueRepository: DefaultIssueRepository = DefaultIssueRepository.getRepository(application)
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    //convert flow to MutableLiveData
    var allIssues: MutableLiveData<List<Issue>> =
        issueRepository.allIssuesFromDb.asLiveData() as MutableLiveData<List<Issue>>


    val idInput = MutableLiveData<Long>()
    val issueById: LiveData<Issue> = Transformations.switchMap(idInput) {
        it -> issueRepository.getIssueByID(it)
    }

    init {

        //testes with coroutines
//        GlobalScope.launch(Dispatchers.IO) {
//            delay(2000)
//            allIssues.postValue(emptyList())
//            delay(20000)
//            mRepository.allIssuesFromDb.collect { list -> allIssues.postValue(list) }
//        }

        //Scheduler task with WorkManager PeriodicWorkRequestBuilder KEEP
        setupWorkManager(application)
    }

    private fun setupWorkManager(application: Application) {
        val updateListWorker: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<UpdateListWorker>(
                15,
                TimeUnit.MINUTES
            ).setInitialDelay(30000, TimeUnit.MILLISECONDS).build()
        WorkManager.getInstance(application)
            .enqueueUniquePeriodicWork(
                "checkUpdate",
                ExistingPeriodicWorkPolicy.KEEP,
                updateListWorker
            )
    }

    fun getIssueByID(id: Long): LiveData<Issue> {
        return issueRepository.getIssueByID(id)
    }

    fun insert(issue: Issue) = viewModelScope.launch(ioDispatcher) {
        issueRepository.insert(issue = issue)
    }

    fun insertList(issues: List<Issue>) = viewModelScope.launch(ioDispatcher) {
        issueRepository.insertList(issues = issues)
    }

    fun getIssue(id: Long): LiveData<Issue> {
        val issue: LiveData<Issue> = issueRepository.getIssueByID(id)
        return issue
    }

}