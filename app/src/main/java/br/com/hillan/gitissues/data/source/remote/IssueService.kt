package br.com.hillan.gitissues.data.source.remote

import androidx.lifecycle.LiveData
import retrofit2.http.GET
import br.com.hillan.gitissues.data.models.Issue

const val BASE_URL_GITISSUE = "https://api.github.com/repos/androiddevbr/vagas/"

interface IssueService {

    // @GET("issues?state=open&per_page=100")
    @GET("issues?state=all")
    fun observeIssues(): LiveData<List<Issue>>

    @GET("issues?state=all")
    suspend fun getIssues(): List<Issue>

}