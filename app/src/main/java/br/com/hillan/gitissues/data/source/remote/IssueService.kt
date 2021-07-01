package br.com.hillan.gitissues.data.source.remote

import retrofit2.Call
import retrofit2.http.GET
import br.com.hillan.gitissues.data.models.Issue

interface IssueService {

    // @GET("issues?state=open&per_page=100")
    @GET("issues?state=all")
    fun list(): Call<List<Issue>>

}