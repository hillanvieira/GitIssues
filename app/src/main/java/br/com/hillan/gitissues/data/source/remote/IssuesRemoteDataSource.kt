package br.com.hillan.gitissues.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import br.com.hillan.gitissues.data.models.Issue
import br.com.hillan.gitissues.data.models.User
import br.com.hillan.gitissues.data.source.IssuesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class IssuesRemoteDataSource @Inject internal constructor(
    private val issueService: IssueService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): IssuesDataSource {

    private val resultIssue: Result<Issue> = success(Issue(0L,"", Date(),"","","", User("")))

    private val liveDataIssue: MutableLiveData<Result<Issue>> by lazy {
        MutableLiveData<Result<Issue>>()
    }

    override fun observeIssues(): LiveData<Result<List<Issue>>> {
        return issueService.observeIssues().map {
           success(it)
        }
    }

    override suspend fun getIssues(): Result<List<Issue>> = withContext(ioDispatcher) {

        try {
            val issue = issueService.getIssues()
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

    override fun observeIssue(issueId: Long): LiveData<Result<Issue>> {
        return  liveDataIssue
    }

    override suspend fun getIssue(issueId: Long): Result<Issue> {
        return resultIssue
    }

    override suspend fun refreshIssue(issueId: Long) {
        //NO-OP
    }

    override fun observeLastIssue(): LiveData<Result<Issue>> {
        return liveDataIssue
    }

    override suspend fun getLastIssue(): Result<Issue> {
        return resultIssue
    }

    override suspend fun saveIssue(issue: Issue) {
        //NO-OP
    }

    override suspend fun deleteAllIssues() {
        //NO-OP
    }

    override suspend fun deleteIssue(issueId: Long) {
        //NO-OP
    }
}