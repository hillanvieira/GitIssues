package br.com.hillan.gitissues.data.source.remote

import br.com.hillan.gitissues.data.source.remote.IssueService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL_GITISSUE = "https://api.github.com/repos/androiddevbr/vagas/"

class RetrofitInitializer {

    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_GITISSUE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun issueService(provideRetrofit: Retrofit): IssueService {
        return provideRetrofit.create(IssueService::class.java)
    }
}