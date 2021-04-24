package br.com.hillan.gitissues.services

import androidx.lifecycle.LiveData
import br.com.hillan.gitissues.models.Issue
import retrofit2.Call
import retrofit2.http.GET

interface IssueService {

    @GET("issues?state=open&per_page=100")
    fun list() : Call<List<Issue>>

}