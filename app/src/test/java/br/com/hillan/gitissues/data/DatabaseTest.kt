package br.com.hillan.gitissues.data

import org.junit.Test
import org.mockito.Mockito
import org.koin.test.KoinTest
import android.content.Context
import kotlin.concurrent.thread
import org.koin.dsl.module.module
import org.koin.standalone.inject
import org.koin.android.ext.koin.with
import org.koin.standalone.StandAloneContext
import br.com.hillan.gitissues.data.source.local.GitIssuesDatabase


class DatabaseTest : KoinTest {

    val appModule = module {
        single { GitIssuesDatabase.getInstance(Mockito.mock(Context::class.java)) }
    }

    private val dbInstace: GitIssuesDatabase by inject()


    @Test
    fun `should get the same instance of Database when run threads simultaneously`() {
        StandAloneContext.startKoin(listOf(appModule)) with (Mockito.mock(Context::class.java))
        repeat(10) {
            thread(start = true) {
                println(dbInstace)
            }
        }
        Thread.sleep(500)
    }

}