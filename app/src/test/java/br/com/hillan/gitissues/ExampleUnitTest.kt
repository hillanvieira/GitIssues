package br.com.hillan.gitissues

import org.junit.Test
import org.junit.Assert.*
import org.koin.test.KoinTest
import android.content.Context
import org.mockito.Mockito.mock
import kotlin.concurrent.thread
import org.koin.dsl.module.module
import org.koin.standalone.inject
import org.koin.android.ext.koin.with
import org.koin.standalone.StandAloneContext.startKoin
import br.com.hillan.gitissues.repository.IssueRepository
import br.com.hillan.gitissues.database.GitIssuesDatabase
import br.com.hillan.gitissues.application.GitIssuesApplication

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

}

