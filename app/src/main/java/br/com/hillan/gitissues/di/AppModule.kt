package br.com.hillan.gitissues.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

//    @Singleton
//    @Provides
//    fun provideIssueServie(): IssueService {
//        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
//        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL_GITISSUE)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(IssueService::class.java)
//    }
//
//    @Singleton
//    @Provides
//    fun provideDb(app: Application): GitIssuesDatabase {
//        return Room.databaseBuilder(
//            app,
//            GitIssuesDatabase::class.java,
//            "issues.db"
//        ).build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideIssueDao(db: GitIssuesDatabase): IssueDao {
//        return db.issueDao()
//    }
//
//    @Singleton
//    @Provides
//    fun provideIssuesLocalDataSource(issueDao: IssueDao): IssuesLocalDataSource {
//        return IssuesLocalDataSource(issueDao)
//    }
//
//    @Singleton
//    @Provides
//    fun provideIssuesRemoteDataSource(issueService: IssueService): IssuesRemoteDataSource {
//        return IssuesRemoteDataSource(issueService)
//    }
//
//    @Singleton
//    @Provides
//    fun provideDefaultIssueRepository(issuesLocalDataSource: IssuesLocalDataSource, issuesRemoteDataSource: IssuesRemoteDataSource): DefaultIssueRepository {
//        return DefaultIssueRepository(issuesLocalDataSource, issuesRemoteDataSource)
//    }

}