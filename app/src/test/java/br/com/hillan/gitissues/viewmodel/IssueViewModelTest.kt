package br.com.hillan.gitissues.viewmodel


import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration

import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import br.com.hillan.gitissues.getOrAwaitValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class IssueViewModelTest {

    //Subject under test
    private lateinit var issueViewModel: IssueViewModel

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {

        // Given
        issueViewModel = IssueViewModel(ApplicationProvider.getApplicationContext())
    }

@Test
fun test(){
   val test = issueViewModel.items.getOrAwaitValue()
   print(test)
}

}