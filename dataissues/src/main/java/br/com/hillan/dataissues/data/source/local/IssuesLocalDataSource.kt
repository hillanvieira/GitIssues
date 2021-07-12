package br.com.hillan.dataissues.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import br.com.hillan.dataissues.data.source.IssuesDataSource
import br.com.hillan.dataissues.data.Issue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success


class IssuesLocalDataSource @Inject constructor(
    private val issueDao: IssueDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IssuesDataSource {

    override fun observeIssues(): LiveData<Result<List<Issue>>> {
        return issueDao.observeIssues().map {
            success(it)
        }
    }

    override fun observeIssue(issueId: Long): LiveData<Result<Issue>> {
        return issueDao.observeIssueById(issueId).map {
            success(it)
        }
    }

    override suspend fun getIssues(): Result<List<Issue>> = withContext(ioDispatcher) {
        return@withContext try {
            success(issueDao.getIssues())
        } catch (e: Exception) {
            failure(e)
        }
    }

    override suspend fun getIssue(issueId: Long): Result<Issue> = withContext(ioDispatcher) {
        try {
            val issue = issueDao.getIssueById(issueId)
            if (issue != null) {
                return@withContext success(issue)
            } else {
                return@withContext failure(Exception("Issue not found!"))
            }
        } catch (e: Exception) {
            return@withContext failure(e)
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
            success(it)
        }
    }

    override suspend fun getLastIssue(): Result<Issue> = withContext(ioDispatcher) {
        return@withContext try {
            success(issueDao.getLastIssue())
        } catch (e: Exception) {
            failure(e)
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