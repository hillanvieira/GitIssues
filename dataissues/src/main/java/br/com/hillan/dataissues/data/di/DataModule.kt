package br.com.hillan.dataissues.data.di

import android.app.Application
import androidx.room.Room
import br.com.hillan.dataissues.data.source.DefaultIssueRepository
import br.com.hillan.dataissues.data.source.local.GitIssuesDatabase
import br.com.hillan.dataissues.data.source.local.IssueDao
import br.com.hillan.dataissues.data.source.local.IssuesLocalDataSource
import br.com.hillan.dataissues.data.source.remote.BASE_URL_GITISSUE
import br.com.hillan.dataissues.data.source.remote.IssueService
import br.com.hillan.gitissues.data.source.remote.IssuesRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Singleton
    @Provides
    fun provideIssueServie(): IssueService {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL_GITISSUE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IssueService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): GitIssuesDatabase {
        return Room.databaseBuilder(
            app,
            GitIssuesDatabase::class.java,
            "issues.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideIssueDao(db: GitIssuesDatabase): IssueDao {
        return db.issueDao()
    }

    @Singleton
    @Provides
    fun provideIssuesLocalDataSource(issueDao: IssueDao): IssuesLocalDataSource {
        return IssuesLocalDataSource(issueDao)
    }

    @Singleton
    @Provides
    fun provideIssuesRemoteDataSource(issueService: IssueService): IssuesRemoteDataSource {
        return IssuesRemoteDataSource(issueService)
    }

    @Singleton
    @Provides
    fun provideDefaultIssueRepository(issuesLocalDataSource: IssuesLocalDataSource, issuesRemoteDataSource: IssuesRemoteDataSource): DefaultIssueRepository {
        return DefaultIssueRepository(issuesLocalDataSource, issuesRemoteDataSource)
    }

}