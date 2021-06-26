package br.com.hillan.gitissues

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import br.com.hillan.gitissues.ui.fragments.ViewIssueFragment

@RunWith(AndroidJUnit4::class)
class FragmentTest {

    @Test
    fun deveAbrirOFragmentEVerSeOTituloDaIsseCorrespondeAoIDPassadoViaArgs() {
        val fragmentArgs = Bundle().apply {
            putLong("issueId", 930410497)
        }

        val scenario = launchFragmentInContainer<ViewIssueFragment>(fragmentArgs)
        onView(withId(R.id.view_issue_title)).check(matches(withText("[Portugal] Senior Android Developer")))
    }

//    @Test
//    fun testListIssueFragment(){
//        val scenario = launchFragmentInContainer<ViewIssueFragment>()
//    }

}