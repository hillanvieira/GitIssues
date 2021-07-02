package br.com.hillan.gitissues.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.hillan.gitissues.data.source.IssueRepository
import br.com.hillan.gitissues.data.source.local.GitIssuesDatabase
import br.com.hillan.gitissues.data.source.remote.RetrofitProvider
import org.junit.Rule

import org.junit.Test

import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IssueViewModelTest{

    // Subject under test
    private lateinit var issueViewModel: IssueViewModel

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    fun setupViewModel(){

        issueViewModel = IssueViewModel(ApplicationProvider.getApplicationContext())
    }


    @Test
    fun getAllIssues_ListOfIssues() {

        setupViewModel()

    }



}