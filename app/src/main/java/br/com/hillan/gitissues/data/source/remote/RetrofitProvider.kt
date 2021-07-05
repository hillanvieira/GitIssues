package br.com.hillan.gitissues.data.source.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL_GITISSUE = "https://api.github.com/repos/androiddevbr/vagas/"

class RetrofitProvider {

    val retrofit: Retrofit
        get() {
            return Retrofit.Builder()
                .baseUrl(BASE_URL_GITISSUE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    val service: IssueService
        get() {
            return retrofit.create(IssueService::class.java)
        }

}