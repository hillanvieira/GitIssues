package br.com.hillan.gitissues.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL_GITISSUE = "https://api.github.com/repos/androiddevbr/vagas/";

class RetrofitInitializer {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_GITISSUE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun issueService(): IssueService {
        return retrofit.create(IssueService::class.java)
    }
}