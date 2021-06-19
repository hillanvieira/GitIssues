package br.com.hillan.gitissues

import android.content.Context
import br.com.hillan.gitissues.database.GitIssuesDatabase
import br.com.hillan.gitissues.repository.IssueRepository
import org.junit.Test
import org.koin.android.ext.koin.with
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mockito
import kotlin.concurrent.thread
import org.koin.android.viewmodel.ext.android.viewModel

class DatabaseTest : KoinTest {

    val appModule = module {
        single { GitIssuesDatabase.getInstance(Mockito.mock(Context::class.java))!!}
        factory { IssueRepository(get<GitIssuesDatabase>().issueDao()) }
    }

    private val dbInstace: GitIssuesDatabase by inject()
    private val repositoryInstance : IssueRepository by inject()


    @Test
    fun `should get the same instance of Database when run threads simultaneously`() {
        StandAloneContext.startKoin(listOf(appModule)) with (Mockito.mock(Context::class.java))
        repeat(10) {
            thread(start = true) {
                println(dbInstace)
                println(repositoryInstance)
            }
        }
        Thread.sleep(500)
    }

}