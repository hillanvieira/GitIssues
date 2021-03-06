package br.com.hillan.dataissues.data.source

import androidx.lifecycle.LiveData
import br.com.hillan.dataissues.data.Issue
import timber.log.Timber
import javax.inject.Inject
import kotlin.Result.Companion.failure

class DefaultIssueRepository @Inject constructor(
    private val issuesLocalDataSource: IssuesDataSource,
    private val issuesRemoteDataSource: IssuesDataSource
) {

    companion object {
        var counter: Int = 0
    }

    init {
        counter++
        Timber.i("INSTANCES_REPOSITORY: $counter")
    }

    suspend fun getIssues(forceUpdate: Boolean = false): Result<List<Issue>> {
        if (forceUpdate) {
            try {
                updateIssuesFromRemoteDataSource()
            } catch (e: Exception) {
                return failure(e)
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

        if (remoteIssues.isSuccess) {
            // Real apps might want to do a proper sync.
            issuesLocalDataSource.deleteAllIssues()

            remoteIssues.getOrNull()!!.forEach { issue ->
                issuesLocalDataSource.saveIssue(issue)
            }

        } else if (remoteIssues.isFailure) {
            throw remoteIssues.exceptionOrNull()!!
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

