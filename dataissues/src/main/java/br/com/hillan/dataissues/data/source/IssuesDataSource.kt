package br.com.hillan.dataissues.data.source

import androidx.lifecycle.LiveData
import br.com.hillan.dataissues.data.Issue

interface IssuesDataSource {

    fun observeIssues(): LiveData<Result<List<Issue>>>

    suspend fun getIssues(): Result<List<Issue>>

    suspend fun refreshIssues()

    fun observeIssue(issueId: Long): LiveData<Result<Issue>>

    suspend fun getIssue(issueId: Long): Result<Issue>

    suspend fun refreshIssue(issueId: Long)

    fun observeLastIssue(): LiveData<Result<Issue>>

    suspend fun getLastIssue(): Result<Issue>

    suspend fun saveIssue(issue: Issue)

    suspend fun deleteAllIssues()

    suspend fun deleteIssue(issueId: Long)

}