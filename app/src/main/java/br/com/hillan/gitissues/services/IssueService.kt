package br.com.hillan.gitissues.services

import br.com.hillan.gitissues.models.Issue
import retrofit2.Call
import retrofit2.http.GET

interface IssueService {

   // @GET("issues?state=open&per_page=100")
    @GET("issues?state=all")
    fun list() : Call<List<Issue>>

}