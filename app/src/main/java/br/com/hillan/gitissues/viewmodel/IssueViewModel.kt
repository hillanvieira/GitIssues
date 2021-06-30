package br.com.hillan.gitissues

import androidx.work.*
import androidx.lifecycle.*
import android.app.Application
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import br.com.hillan.gitissues.models.Issue
import br.com.hillan.gitissues.services.UpdateListWorker
import br.com.hillan.gitissues.repository.IssueRepository

class IssueViewModel(
    private val  mRepository: IssueRepository, application: Application
) : AndroidViewModel(application) {

    //convert flow to MutableLiveData
    var allIssues: MutableLiveData<List<Issue>> =
        mRepository.allIssuesFromDb.asLiveData() as MutableLiveData<List<Issue>>

    val idInput = MutableLiveData<Long>()
    val issueById: LiveData<Issue> = Transformations.switchMap(idInput) {
        it -> mRepository.getIssueByID(it)
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
        return mRepository.getIssueByID(id)
    }

    fun insert(issue: Issue) = viewModelScope.launch(Dispatchers.IO) {
        mRepository.insert(issue = issue)
    }

    fun insertList(issues: List<Issue>) = viewModelScope.launch(Dispatchers.IO) {
        mRepository.insertList(issues = issues)
    }

    fun getIssue(id: Long): LiveData<Issue> {
        val issue: LiveData<Issue> = mRepository.getIssueByID(id)
        return issue
    }

}