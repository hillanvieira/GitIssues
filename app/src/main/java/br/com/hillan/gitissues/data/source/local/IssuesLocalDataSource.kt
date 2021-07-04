package br.com.hillan.gitissues.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import br.com.hillan.gitissues.data.source.IssuesDataSource
import br.com.hillan.gitissues.data.models.Issue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import br.com.hillan.gitissues.data.Result.Success
import br.com.hillan.gitissues.data.Result.Error
import br.com.hillan.gitissues.data.Result


class IssuesLocalDataSource internal constructor(
    private val issueDao: IssueDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IssuesDataSource {

    override fun observeIssues(): LiveData<Result<List<Issue>>> {
        return issueDao.observeIssues().map {
            Success(it)
        }
    }

    override fun observeIssue(issueId: Long): LiveData<Result<Issue>> {
        return issueDao.observeIssueById(issueId).map {
            Success(it)
        }
    }

    override suspend fun getIssues(): Result<List<Issue>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(issueDao.getIssues())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getIssue(issueId: Long): Result<Issue> = withContext(ioDispatcher) {
        try {
            val issue = issueDao.getIssueById(issueId)
            if (issue != null) {
                return@withContext Success(issue)
            } else {
                return@withContext Error(Exception("Issue not found!"))
            }
        } catch (e: Exception) {
            return@withContext Error(e)
        }
    }

    override suspend fun refreshIssues() {
        //NO-OP
    }

    override suspend fun refreshIssue(issueId: Long) {
        //NO-OP
    }

    override fun observeLastIssue(): LiveData<Result<Issue>> {
        return issueDao.observeLastIssue().map {
            Success(it)
        }
    }

    override suspend fun getLastIssue(): Result<Issue> = withContext(ioDispatcher) {
        return@withContext try {
            Success(issueDao.getLastIssue())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun saveIssue(issue: Issue) = withContext(ioDispatcher) {
        issueDao.insertIssue(issue)
    }

    override suspend fun deleteAllIssues() = withContext(ioDispatcher) {
        issueDao.deleteIssues()
    }

    override suspend fun deleteIssue(issueId: Long) {
        issueDao.deleteIssueById(issueId)
    }

}