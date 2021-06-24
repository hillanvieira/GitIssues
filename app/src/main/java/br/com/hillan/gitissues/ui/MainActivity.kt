package br.com.hillan.gitissues.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.hillan.gitissues.R
import br.com.hillan.gitissues.models.Issue

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
          setContentView(R.layout.issues_host)
    }

}

