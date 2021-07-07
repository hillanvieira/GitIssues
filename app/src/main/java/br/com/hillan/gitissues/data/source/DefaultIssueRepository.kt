package br.com.hillan.gitissues.data.source

import android.app.Application
import androidx.lifecycle.LiveData
import br.com.hillan.gitissues.data.Result
import br.com.hillan.gitissues.data.Result.Error
import br.com.hillan.gitissues.data.Result.Success
import br.com.hillan.gitissues.data.models.Issue
import timber.log.Timber
import javax.inject.Inject


class DefaultIssueRepository @Inject constructor(
    private val issuesLocalDataSource: IssuesDataSource,
    private val issuesRemoteDataSource: IssuesDataSource
) {

    companion object {

        var counter: Int = 0
        fun counter(): Int {
            return counter
        }
    }

    init {
        counter++
        Timber.i("INSTANCES_REPOSITORY")
    }

    suspend fun getIssues(forceUpdate: Boolean = false): Result<List<Issue>> {
        if (forceUpdate) {
            try {
                updateIssuesFromRemoteDataSource()
            } catch (ex: Exception) {
                return Error(ex)
            }
        }
        return issuesLocalDataSource.getIssues()
    }

    suspend fun refreshIssues() {
        updateIssuesFromRemoteDataSource()
    }

    fun observeIssues(): LiveData<Result<List<Issue>>> {
        return issuesLocalDataSource.observeIssues()
    }

    private suspend fun updateIssuesFromRemoteDataSource() {
        val remoteIssues = issuesRemoteDataSource.getIssues()

        if (remoteIssues is Success) {
            // Real apps might want to do a proper sync.
            issuesLocalDataSource.deleteAllIssues()
            remoteIssues.data.forEach { issue ->
                issuesLocalDataSource.saveIssue(issue)
            }
        } else if (remoteIssues is Error) {
            throw remoteIssues.exception
        }
    }

    fun observeIssue(issueId: Long): LiveData<Result<Issue>> {
        return issuesLocalDataSource.observeIssue(issueId)
    }


    suspend fun getIssue(issueId: Long, forceUpdate: Boolean = false): Result<Issue> {
        if (forceUpdate) {
            updateIssuesFromRemoteDataSource()
        }
        return issuesLocalDataSource.getIssue(issueId)
    }

    private suspend fun getIssueWithId(id: Long): Result<Issue> {
        return issuesLocalDataSource.getIssue(id)
    }

    suspend fun getLastIssue(): Result<Issue> {
        return issuesLocalDataSource.getLastIssue()
    }

}

